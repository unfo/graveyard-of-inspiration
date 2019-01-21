#!/usr/bin/python2.7

from PIL import Image
import sys
import os

def pikturize(filename):
	with open(filename,'rb') as f:
		ln = os.path.getsize(filename)
		width = 554;
		rem = ln%width; 
		height = ((ln-rem) / width)
		i = Image.new('L', (width, height), 0)

		x = y = 0
		while y < height:
			bs = f.read(9001)

			for b in bs:
				i.putpixel((x,y), ord(b))
				x += 1
				if x == width:
					x = 0
					y += 1

		fname = filename + '.jpg'
		with open(fname,'w') as newf:
			i.save(newf)
		
		print 'pikturized as %s' % fname











if len(sys.argv) == 2:
	fname = sys.argv[1]
 	print 'You want me to print image for %s' % fname
 	pikturize(fname)
else:
	print 'Usage: %s <filename>' % sys.argv[0]
	exit(1)

