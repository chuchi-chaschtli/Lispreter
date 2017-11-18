;; higher-order function examples
;; /test/files/ho-funcs.lisp -> /test/files/ho-funcs.txt
(defun map (func lst)
    (if (endp lst)
        NIL
        (cons (func (car lst))
              (map func (cdr lst)))))
              
(defun andmap (func lst)
    (if (endp lst)
        T
        (and (func (car lst))
             (andmap func (cdr lst)))))
             
(defun ormap (func lst)
    (if (endp lst)
        NIL
        (or (func (car lst))
            (ormap func (cdr lst)))))

(defun filter (pred lst)
    (cond [(endp lst) NIL]
          [(pred (car lst)) (cons (car lst) (filter pred (cdr lst)))]
          [t (filter pred (cdr lst))]))
          
(map integerp (list 1 2 "abc" T))
(andmap integerp (list 1 2 "abc" T))
(andmap integerp (list 1 2 3 4))
(ormap integerp (list 1 2 "abc" T))
(ormap integerp (list NIL T "xyz"))
(filter integerp (list 1 2 "abc" 4))
(filter integerp (list 1 2 3))