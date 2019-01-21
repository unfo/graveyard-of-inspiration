#!/usr/bin/python

primekeys = {}
primes = []

with open('../prime-per-line.txt', 'r') as f:
    contents = f.readlines()

for line in contents:
    number = int(line)
    primekeys[number] = True
    primes.append(number)


def sum_is_prime(n):
    nums = str(n)
    total = 0
    for num in nums:
        total += int(num)

    return total in primes


i = 0
found_primes = 0
ret_str = ""
# while found_primes < 2:
#     prime = primes[i]
#     if prime > 1000000:
#         if sum_is_prime(prime):
#             ret_str += str(prime)
#             found_primes += 1
#     i += 1

# target = 2015 = 403 * 5

# print ret_str
