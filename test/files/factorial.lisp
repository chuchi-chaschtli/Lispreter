# /test/files/factorial.lisp -> /test/files/fact-output.txt
(defun factorial (n)
	(if (eq n 0)
		1
		(product n (factorial (difference n 1)))))
		
(factorial 0)
(factorial 1)
(factorial 2)
(factorial 3)
(factorial 4)