;; higher-order function examples
;; /test/integration/ho-funcs.lisp -> /test/integration/ho-funcs.txt

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
    
;; [List-of Any] [List-of Any] -> [List-of Any]
(defun append (l m)
    (cond [(endp l) m]
          [t (cons (car l) (append (cdr l) m))]))
          
;; [List-of Any] [List-of Any] -> [List-of Any]
(defun interleave (l m)
    (cond [(endp l) m]
          [(endp m) l]
          [t (cons (car l) (cons (car m) (interleave (cdr l) (cdr m))))]))
        
;; Not currently working :[
(defun map2 (f lst)
	(foldr (lambda (x z) (cons (f x) z)) NIL lst))
	
(defun andmap2 (f lst)
    (foldr (lambda (x z) (and (f x) z)) T lst))
        
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
(append (list 1 2 3) (list 4 5 6))
(append (cons 1 (cons 2 (cons 3 NIL))) (cons 4 (cons 5 (cons 6 (cons 7 NIL)))))
(append (list 1 2 3 4) NIL)
(append NIL (list 1 2 3 4))
(interleave (list 1 3 5) (list 2 4 6))
(interleave (cons 1 (cons 2 (cons 3 NIL))) (cons 4 (cons 5 (cons 6 (cons 7 NIL)))))
(interleave (list 1 2 3 4) NIL)
(interleave NIL (list 1 2 3 4))
;;(map2 integerp (list 1 2 "abc" 5 T))
(andmap2 integerp (list 1 2 3 4))
(andmap2 integerp (list 2 3 "abc" 3))