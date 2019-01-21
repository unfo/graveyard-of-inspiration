def my_func(b=[]):
    if not b:
        b.append(0)
    b[0] += 1
    print b

my_func()
my_func()
my_func()
