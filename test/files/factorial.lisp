# /test/files/factorial.lisp -> /test/files/fact-output.txt
(defun factorial (n)
	(if (<= n 1)
		1
		(* n (factorial (- n 1)))))
		
(factorial 0)
(factorial 1)
(factorial 2)
(factorial 3)
(factorial 4)