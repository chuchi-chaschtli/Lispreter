;; higher-order function examples
;; /test/files/ho-funcs.lisp -> /test/files/ho-funcs.txt

;; [X -> Y] [List-of X] -> [List-of Y]
(defun map (func lst)
    (if (endp lst)
        NIL
        (cons (func (car lst))
              (map func (cdr lst)))))

;; [X -> Boolean] [List-of X] -> [List-of Boolean]
(defun andmap (func lst)
    (if (endp lst)
        T
        (and (func (car lst))
             (andmap func (cdr lst)))))

;; [X -> Boolean] [List-of X] -> [List-of Boolean]    
(defun ormap (func lst)
    (if (endp lst)
        NIL
        (or (func (car lst))
            (ormap func (cdr lst)))))

;; [X -> Boolean] [List-of X] -> [List-of X]
(defun filter (pred lst)
    (cond [(endp lst) NIL]
          [(pred (car lst)) (cons (car lst) (filter pred (cdr lst)))]
          [t (filter pred (cdr lst))]))

;; [X -> Y] [Y] [List-of X] -> [List-of Y]
(defun foldr (func y lst)
    (if (endp lst)
        y
        (func (car lst) (foldr func y (cdr lst)))))
        
;; [X -> Y] [Y] [List-of X] -> [List-of Y]
(defun foldl (func y lst)
    (if (endp lst)
        y
        (foldl func (func y (car lst)) (cdr lst))))

;; [List-of Num] -> [List-of Num]        
(defun addten (lst)
    (map (lambda (x) (+ 10 x)) lst))
	
;; [X -> Y] [Y -> Z] [List-of X] -> [List-of Z]
(defun composeList (f g lst)
    (map (lambda (x) (f (g x))) lst))
        
;; Not currently working :[
(defun map2 (func lst)
	(foldr (lambda (x z) (cons (func x) z)) NIL lst))
          
(map integerp (list 1 2 "abc" T))
(andmap integerp (list 1 2 "abc" T))
(andmap integerp (list 1 2 3 4))
(ormap integerp (list 1 2 "abc" T))
(ormap integerp (list NIL T "xyz"))
(filter integerp (list 1 2 "abc" 4))
(filter integerp (list 1 2 3))
(foldr + 0 (list 1 2 3 4))
(foldr + 0 NIL)
(foldr - 0 (list 1 2 3 4))
(foldl + 0 (list 1 2 3 4))
(foldl + 0 NIL)
(foldl - 0 (list 1 2 3 4))
(addten (list 3 5 7 9))
(composeList not integerp (list 1 2 "abc" T))
;;(map2 integerp (list 1 2 "abc" 5 T))