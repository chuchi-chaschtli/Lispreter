;; test/integration/input.lisp -> corresponding output target : test/integration/output.txt
; this is a comment.
; comments are ignored by the lexer.
(sum 5 6 7)

;;; comments can be any line dedicated to comments only, and may have any number of ';' signs.
(product (sum 3 3) (sum 2 2)) ;; this is in end-of-line comment.

(defun sumpartials (x y z) (sum (product x y) (product x z) (product y z)))

(sumpartials 3 4 5)
(sumpartials 4 5 6)
(eq (sumpartials 3 4 5) 47)
(eq (sumpartials 3 4 5) (sumpartials 4 5 3))

((lambda (x) (sumpartials x x x)) 4)
(defun lambdasumpartial (x) ((lambda (y) (sumpartials y y y)) x))
(lambdasumpartial 4)

(defun nestedlambdasumpartial (x) ((lambda (y) (product 2 ((lambda (z) (sumpartials z z z)) y))) x))
(nestedlambdasumpartial 4)

(not T)
(not NIL)

(and T NIL NIL)
(and T T T)
(or T NIL NIL)
(or NIL NIL)