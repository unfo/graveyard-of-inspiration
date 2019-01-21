#!/usr/bin/python

import sys


def min_needed(percentage, decimals):
    top = 1.0
    bottom = 1.0
    percentage = round(percentage, decimals)
    current = round(100 * (top / bottom), decimals)

    while current != percentage:
        if current < percentage:
            top += 1.0
        
            # 2 / 1 is useless in searching for fractions
            while top >= bottom:
                bottom += 1.0
        
        else:
            bottom += 1.0

        current = round(100 * (top / bottom), decimals)

    return top, bottom


if len(sys.argv) == 3:
    percentage = float(sys.argv[1])
    decimals = int(sys.argv[2])
    print 'Q: How many people does it to make accuracy of %f%% with %d decimal points' % (round(percentage, decimals), decimals)
    (top,bottom) = min_needed(percentage, decimals)
    print 'A: %d / %d = %f' % (top,bottom, round(100 * top/bottom, decimals)) 
else:
    print 'Usage: %s <xx.y> <decimals>' % sys.argv[0]
    exit(1)
