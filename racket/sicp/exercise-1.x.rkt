#lang racket

(define (sum-of-biggest-squares a b c)
  (cond ((and (> a c) (> b c)) (sum-squares a b))
        ((and (> a c) (> c b)) (sum-squares a c))
        (else (sum-squares b c))))

(define (sum-squares a b)
  (+ (square a) (square b)))

(define (square a) (* a a))

