#!/usr/local/bin/ruby
require 'socket'

puts Socket.gethostbyname('jw.dy.fi')[0].to_s

