#!/usr/bin/python2.7

from PIL import Image
import sys
import os

histograms = {}
multiples = []
i = 0
for filename in os.listdir(os.getcwd()):
    try:
        i += 1
        if i % 10 == 0:
            sys.stdout.write('%d ' % i)
            sys.stdout.flush()

        if i % 100 == 0:
            sys.stdout.write('\n')
            sys.stdout.flush()

        with Image.open(filename) as im:
            key = "".join([str(x) for x in im.histogram()])
            if key in histograms:
                histograms[key].append(filename)
                multiples.append(key)
            else:
                histograms[key] = [filename]

    except IOError:
        pass

print 'Found %d similar images' % len(multiples)

for index, key in enumerate(multiples):
    print "Similar: "
    for fn in histograms[key]:
        output, retval = os.system('/usr/local/opt/coreutils/libexec/gnubin/sha1sum "%s"' % fn)
        print output 

    print "---------"