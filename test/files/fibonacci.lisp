;; /test/files/fibonacci.lisp -> /test/files/fib-output.txt
;; This is super inefficient, in general and when passed through the interpreter
;; But so far, the support isn't written to do this the dynamic way :(
(defun fibonacci (n)
	(cond
		[(<= n 1) n]
		[t (+ (fibonacci (- n 1)) 
			  (fibonacci (- n 2)))]))
				
(fibonacci 0)
(fibonacci 1)
(fibonacci 2)
(fibonacci 3)
(fibonacci 4)
(fibonacci 7) ;; at this point it really starts to slow down...