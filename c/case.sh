#!/bin/bash

case "$1" in
	1)
	VAL="I"
	2)
	VAL="II"
	3)
	VAL="III"
	echo "$1 = $VAL"
	;;
	*)
	echo "can't count that high"
esac