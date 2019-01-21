module Test
    where
maxlist :: Int -> [Int] -> Int
maxlist a [] = a
maxlist a (x:xs) | a > x = maxlist a xs
                 | otherwise = maxlist x xs
divisors :: Int -> Int
divisors n = length [i | i <- [1..n], mod n i == 0]
--divs :: [Integer]
--divs = [(i, ((div (i^2 + i) 2), divisors(div (i^2 + i) 2))) | i <- [1..]]
primes = sieve [ 2.. ]
         where
         sieve (p:x) = p : sieve [ n | n <- x, n `mod` p > 0]
