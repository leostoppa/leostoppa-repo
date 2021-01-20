
type ide = string;;
type exp = Eint of int
         | Ebool of bool 
         | Den of ide 
         | Prod of exp * exp 
         | Sum of exp * exp 
         | Diff of exp * exp 
         | Eq of exp * exp 
         | Minus of exp 
         | IsZero of exp 
         | Or of exp * exp 
         | And of exp * exp 
         | Not of exp 
         | Ifthenelse of exp * exp * exp 
         | Let of ide * exp * exp 
         | Fun of ide * exp 
         | FunCall of exp * exp 
         | Letrec of ide * exp * exp
         | Dict of dictBody
         | Insert of ide * exp * exp 
         | Delete of ide * exp
         | HasKey of ide * exp 
         | Iterate of exp * exp
         | Fold of exp * exp 
         | Filter of (ide list) * exp
         | FunAcc of ide * ide * exp
         | FunCallAcc of exp * exp * exp
and dictBody = Empty | Item of ide * exp * dictBody;;

(*ambiente polimorfo*)
type 't env = ide -> 't;;
let emptyenv (v : 't) = function x -> v;;
let applyenv (r : 't env) (i : ide) = r i;;
let bind (r : 't env) (i : ide) (v : 't) = function x -> if x = i then v else applyenv r x;;

(*tipi esprimibili*)
type evT = Int of int 
         | Bool of bool 
         | Unbound 
         | FunVal of evFun 
         | RecFunVal of ide * evFun
         | DictVal of (ide * evT) list
         | FunAccVal of ide * ide * exp * evT env
and evFun = ide * exp * evT env

(*rts*)
(*type checking*)
let rec typecheck (s : string) (v : evT) : bool = match s with
  | "int" -> (match v with
               | Int(_) -> true 
               | _ -> false) 
  | "bool" -> (match v with
                | Bool(_) -> true 
                | _ -> false) 
  | "dictval" -> (match v with 
                   | DictVal (l) -> (match l with 
                                      | [] -> true
                                      | (i,el) :: ls -> (match el with 
                                                          | Int (i) -> typecList "int" l
                                                          | Bool (i) -> typecList "bool" l
                                                          | _ -> failwith ("invalid dict value type")))
                   | _ -> false)
  | _ -> failwith ("Not a valid type")
and typecList (s : string) (l : (ide * evT) list) : bool = (match l with 
                                                             | [] -> true
                                                             | (i,el) :: ls -> (typecheck s el)&&(typecList s ls));;

(*funzioni primitive*)
let prod x y = if (typecheck "int" x) && (typecheck "int" y)
  then (match (x,y) with
           (Int(n),Int(u)) -> Int(n*u)
         |(_,_) -> failwith ("Type error"))
  else failwith("Type error");;

let sum x y = if (typecheck "int" x) && (typecheck "int" y)
  then (match (x,y) with
           (Int(n),Int(u)) -> Int(n+u)
         |(_,_) -> failwith ("Type error"))
  else failwith("Type error");;

let diff x y = if (typecheck "int" x) && (typecheck "int" y)
  then (match (x,y) with
           (Int(n),Int(u)) -> Int(n-u)
         |(_,_) -> failwith ("Type error"))
  else failwith("Type error");;

let eq x y = if (typecheck "int" x) && (typecheck "int" y)
  then (match (x,y) with
           (Int(n),Int(u)) -> Bool(n=u)
         |(_,_) -> failwith ("Type error"))
  else failwith("Type error");;

let minus x = if (typecheck "int" x)
  then (match x with
           Int(n) -> Int(-n)
         |_ -> failwith ("Type error"))
  else failwith("Type error");;

let iszero x = if (typecheck "int" x)
  then (match x with
           Int(n) -> Bool(n=0)
         |_ -> failwith ("Type error"))
  else failwith("Type error");;

let vel x y = if (typecheck "bool" x) && (typecheck "bool" y)
  then (match (x,y) with
           (Bool(b),Bool(e)) -> (Bool(b||e))
         |(_,_) -> failwith ("Type error"))
  else failwith("Type error");;

let et x y = if (typecheck "bool" x) && (typecheck "bool" y)
  then (match (x,y) with
           (Bool(b),Bool(e)) -> Bool(b&&e)
         |(_,_) -> failwith ("Type error"))
  else failwith("Type error");;

let non x = if (typecheck "bool" x)
  then (match x with
           Bool(true) -> Bool(false) |
           Bool(false) -> Bool(true)
         |_ -> failwith ("Type error"))
  else failwith("Type error");;

let insert (i : ide) (v : evT) (l : (ide * evT) list) : (ide * evT) list = 
  (match v with 
    | Int (n) -> if (typecList "int" l) then l @ [(i,Int(n))] 
        else failwith ("Type error")
    | Bool (n) -> if (typecList "bool" l) then l @ [(i,Bool(n))]
        else failwith ("Type error")
    | _ -> failwith ("Invalid dict value"));;

let rec delete (i : ide) (l : (ide * evT) list) : (ide * evT) list = 
  match l with 
    | [] -> []
    | (key,v) :: ls -> if (compare key i == 0) then ls 
        else (key,v) :: delete i ls;; 

let rec haskey (i : ide) (l : (ide * evT) list) : evT = 
  match l with 
    | [] -> Bool(false) 
    | (key,v) :: ls -> if (compare i key == 0) then Bool(true) 
        else haskey i ls;;

(*interprete*)
let rec eval (e : exp) (r : evT env) : evT = match e with
  | Eint n -> Int n 
  | Ebool b -> Bool b 
  | IsZero a -> iszero (eval a r) 
  | Den i -> applyenv r i 
  | Eq(a, b) -> eq (eval a r) (eval b r) 
  | Prod(a, b) -> prod (eval a r) (eval b r) 
  | Sum(a, b) -> sum (eval a r) (eval b r) 
  | Diff(a, b) -> diff (eval a r) (eval b r) 
  | Minus a -> minus (eval a r) 
  | And(a, b) -> et (eval a r) (eval b r) 
  | Or(a, b) -> vel (eval a r) (eval b r) 
  | Not a -> non (eval a r) 
  | Ifthenelse(a, b, c) -> 
      let g = (eval a r) in
        if (typecheck "bool" g) 
        then (if g = Bool(true) then (eval b r) else (eval c r))
        else failwith ("nonboolean guard") 
  | Let(i, e1, e2) -> eval e2 (bind r i (eval e1 r)) 
  | Fun(i, a) -> FunVal(i, a, r)
  | FunAcc (acc,i,a) -> FunAccVal (acc,i,a,r)
  | FunCall(f, eArg) -> 
      let fClosure = (eval f r) in
        (match fClosure with
          | FunVal(arg, fBody, fDecEnv) -> 
              eval fBody (bind fDecEnv arg (eval eArg r)) 
          | RecFunVal(g, (arg, fBody, fDecEnv)) -> 
              let aVal = (eval eArg r) in
              let rEnv = (bind fDecEnv g fClosure) in
              let aEnv = (bind rEnv arg aVal) in
                eval fBody aEnv 
          | _ -> failwith("non functional value")) 
  | FunCallAcc (fa, n, eArg) -> 
      let faClosure = eval fa r in 
        (match faClosure with 
          | FunAccVal (acc, arg, fBody, fDecEnv) -> 
              let v = eval n r in 
              let aVal = eval eArg r in 
              let aEnv = bind fDecEnv arg aVal in 
              let accEnv = bind aEnv acc v in 
                eval fBody accEnv
          | _ -> failwith ("not a function with acc"))
  | Letrec(f, funDef, letBody) ->
      (match funDef with
        | Fun(i, fBody) -> let r1 = (bind r f (RecFunVal(f, (i, fBody, r)))) in
              eval letBody r1 
        | _ -> failwith("non functional def"))
  | Dict (l) -> let dVal = DictVal (evalDict l r) in 
        if (typecheck "dictval" dVal) then dVal
        else failwith ("Type error : dict must be homogeneous!")
  | Insert (i,e,d) -> 
      (match eval d r with 
        | DictVal (b) -> let v = eval e r in 
              DictVal (insert i v b)
        | _ -> failwith ("This operation must be applied to a dict type"))
  | Delete (i,d) -> 
      (match eval d r with 
        | DictVal (b) -> DictVal (delete i b)  
        | _ -> failwith ("This operation must be applied to a dict type"))
  | HasKey (i,d) -> 
      (match eval d r with 
        | DictVal (b) -> haskey i b
        | _ -> failwith ("This operation must be applied to a dict type"))
  | Iterate (f,d) -> 
      (match eval d r with 
        | DictVal(l) -> DictVal(iterate f l r) 
        | _ -> failwith ("This operation must be applied to a dict type"))
  | Fold (f,d) -> 
      (match eval d r with
        | DictVal (l) -> 
            (match l with 
              | (key,v) :: ls -> (match v with  
                                   | Int(i) -> (match f with 
                                                 | FunAcc (acc,i,a) -> (match a  with
                                                                         | Sum (e1,e2) -> eval (fold f (Eint(0)) l r) r
                                                                         | Diff (e1,e2) -> eval (fold f (Eint(0)) l r) r
                                                                         | Prod (e1,e2) -> eval (fold f (Eint(1)) l r) r
                                                                         | _ -> failwith ("Unsupported function with fold"))
                                                 | _ -> failwith ("Not a fun with acc"))
                                   | Bool(i) -> (match f with 
                                                  | FunAcc (acc,i,a) -> (match a  with
                                                                          | And (e1,e2) -> eval (fold f (Ebool(true)) l r) r
                                                                          | Or (e1,e2) -> eval (fold f (Ebool(false)) l r) r
                                                                          | _ -> failwith ("Unsupported function with fold"))
                                                  | _ -> failwith ("Not a fun with acc"))
                                   | _ -> failwith ("Not a dict argument type"))
              | [] -> failwith ("Empty Dict"))
        | _ -> failwith ("This operation must be applied to a dict type"))
  | Filter (li,d) -> 
      (match eval d r with 
        | DictVal (l) -> DictVal(filter li l)
        | _ -> failwith ("This operation must be applied to a dict type"))

and evalDict (l : dictBody) (r : evT env) : (ide * evT) list = 
  (match l with 
    | Empty -> []
    | Item(i,e,ls) -> let v = eval e r in (i,v) :: evalDict ls r)

and iterate (f : exp) (l : (ide * evT) list) (r : evT env)  : (ide * evT) list =
  (match l with 
    | [] -> []
    | (key,e) :: ls -> match e with 
      | Int(n) -> let v = (eval (FunCall(f,Eint(n))) r) in (key,v) :: (iterate f ls r)
      | Bool(n) -> let v = (eval (FunCall(f,Ebool(n))) r) in (key,v) :: (iterate f ls r)
      | _ -> failwith ("Type error"))

and fold (f : exp) (acc : exp) (l : (ide * evT) list) (r : evT env) : exp =  (* TO DO : CONTROLLA SE F E' UNA FUNZIONE*)
  (match l with 
    | [] -> acc
    | (key,el) :: ls -> 
        match el with 
          | Int(n) -> let newAcc = FunCallAcc(f,acc,Eint(n)) in (fold f newAcc ls r)
          | Bool(n) -> let newAcc = FunCallAcc(f,acc,Ebool(n)) in (fold f newAcc ls r)
          | _ -> failwith ("Type dict item error"))

and filter (li : ide list) (l : (ide * evT) list) : (ide * evT) list = 
  (match l with 
    | [] -> []
    | (key,v) :: ls -> if ( List.mem key li ) then (key,v) :: filter li ls 
        else filter li ls);;

(* =============================  TESTS  ============================*)


(*TESTO LE NUOVE OPERAZIONI SU DIZIONARI *)


let env0 = emptyenv Unbound;;



(*=====TEST DICT=====*)



(*ERRORE : PROVO A CREARE UN DIZIONARIO NON OMOGENEO*)
let magazzinoErr0 = Dict(Item("mele", Eint(430), 
                              Item("banane", Ebool(true), 
                                   Item("arance", Eint(525), 
                                        Item("pere", Eint(217), Empty)))));;

eval magazzinoErr0 env0;;


(*APPLICO DICT CORRETTAMENTE*)

(*DIZIONARIO CON VALORI INTERI*)
let magazzinoInt = Dict(Item("mele", Eint(430), 
                             Item("banane", Eint(312), 
                                  Item("arance", Eint(525), 
                                       Item("pere", Eint(217), Empty)))));;

eval magazzinoInt env0;;

(*DIZIONARIO DI BOOLEANI*)
let magazzinoBool = Dict(Item("mele", Ebool(true), 
                              Item("banane", Ebool(false), 
                                   Item("arance", Ebool(true), 
                                        Item("pere", Ebool(false), Empty)))));;

eval magazzinoBool env0;;



(*=====TEST INSERT=====*)



(*ERRORE : PROVO A INSERIRE UN NUOVO ELEMENTO NEL DIZIONARIO CON UN TIPO NON AMMISSIBILE PER IL DIZIONARIO*)
let magazzinoErr1 = Insert ("kiwi", Fun("x",Sum(Den("x"),Eint(1))), magazzinoInt);;

eval magazzinoErr1 env0;;


(*ERRORE : INSERISCO UN NUOVO ELEMENTO DI TIPO DIVERSO DA QUELLO DEL DIZIONARIO*)
let magazzinoErr2 = Insert ("kiwi", Ebool(true), magazzinoInt);;

eval magazzinoErr2 env0;;


(*ERRORE : PROVO A INSERIRE UN NUOVO ELEMENTO NON IN UN DIZIONARIO*)
let magazzinoErr3 = Insert ("kiwi", Eint(300), Eint(1));;

eval magazzinoErr3 env0;;


(*INSERISCO UN NUOVO ELEMENTO NEL DIZIONARIO CORRETTAMENTE*)
let magazzinoInt = Insert ("kiwi", Eint(300), magazzinoInt);;

eval magazzinoInt env0;;


let magazzinoBool = Insert ("kiwi", Ebool(true), magazzinoBool);;

eval magazzinoBool env0;; 



(*=====TEST DELETE=====*)



(*ERRORE : PROVO A CANCELLARE UN ELEMENTO NON IN UN DIZIONARIO*)
let magazzinoErr4 = Delete ("pere", Eint(1));;

eval magazzinoErr4 env0;;


(*CANCELLO UN ELEMENTO DEL DIZIONARIO CORRETTAMENTE *)
let magazzinoInt = Delete ("pere", magazzinoInt);;

eval magazzinoInt env0;;


let magazzinoBool = Delete ("pere", magazzinoBool);;

eval magazzinoBool env0;;



(*=====TEST HASKEY=====*)



(*ERRORE : PROVO A CONTROLLARE SE UNA CHIAVE ESISTE NON IN UN DIZIONARIO*)
let bErr = HasKey ("banane", Eint(1));;

eval bErr env0;;


(*CONTROLLO SE UNA CHIAVE APPARTIENE AL DIZIONARIO CORRETTAMENTE*)
let bInt = HasKey ("banane", magazzinoInt);;

eval bInt env0;;


let bBool = HasKey ("banane", magazzinoBool);;

eval bBool env0;;



(*=====TEST ITERATE=====*)



(*PROVO AD APPLICARE NON UNA FUNZIONE A UN DIZIONARIO*)
let magazzinoErr5 = Iterate (Eint(1), magazzinoInt);;

eval magazzinoErr5 env0;;


let fInt = Fun ("y",Sum(Den("y"),Eint(1)));;


(*ERRORE : PROVO AD APPLICARE UNA FUNZIONE NON A UN DIZIONARIO*)
let magazzinoErr6 = Iterate (fInt, Eint(1));;

eval magazzinoErr6 env0;;


(*APPLICO UNA FUNZIONE A TUTTI GLI ELEMENTI DEL DIZIONARIO CORRETTAMENTE*)
let magazzinoInt = Iterate (fInt, magazzinoInt);;

eval magazzinoInt env0;;


let fBool = Fun ("y",Or(Den("y"),Ebool(true)));;

let magazzinoBool = Iterate (fBool, magazzinoBool);;

eval magazzinoBool env0;;



(*=====TEST FOLD=====*)



let fAccInt = FunAcc ("acc","y",Sum(Den("acc"),Sum(Den("y"),Eint(1))));;


(*APPLICO LA FOLD NON A UN DIZIONARIO*)
let foldMagErr1 = Fold (fAccInt, Eint(1));;

eval foldMagErr1 env0;;


(*APPLICO LA FOLD AL DIZIONARIO CORRETTAMENTE*)
let foldMagInt = Fold (fAccInt, magazzinoInt);;

eval foldMagInt env0;;


let fAccBool = FunAcc ("acc","y",And(Den("acc"),Not(Den("y"))));;

let foldMagBool = Fold (fAccBool, magazzinoBool);;

eval foldMagBool env0;;


(*ERRORE : PROVO AD APPLICARE UNA FUNZIONE NON SUPPORTATA PER LA FOLD SUL DIZIONARIO*)
let foldMagErr2 = Fold(fAccInt, magazzinoBool);;

eval foldMagErr2 env0;;


let foldMagErr3 = Fold(fAccBool, magazzinoInt);;

eval foldMagErr3 env0;;



(*=====TEST FILTER=====*)



let keyList = ["mele"; "pere"];;


(*ERRORE : PROVO A FILTRARE CON UNA LISTA DI CHIAVI NON UN DIZIONARIO*)
let magazzinoErr7 = Filter (keyList, Eint(1));;

eval magazzinoErr7 env0;;


(*FILTRO IL DIZIONARIO CON UNA LISTA DI CHIAVI CORRETTAMENTE*)
let magazzinoInt = Filter (keyList, magazzinoInt);;

eval magazzinoInt env0;;


(*FILTRO IL DIZIONARIO CON UNA LISTA VUOTA*)
let magazzinoInt = Filter ([],magazzinoInt);;

eval magazzinoInt env0;;


