#! /usr/local/bin/ruby -w
require 'net/http'
require 'base64'

ip = logmsg = nil
response = Net::HTTP.get('jw.fi', '/ip/ip.php')
response.each_line do |line|
if line =~ /((\d{1,3}\.?){4})/
    ip = $1
  end
end
unless ip.nil?
  username = Base64.encode64('unfo')
  password = Base64.encode64('asmodeus')
  update = Net::HTTP.get('members.dyndns.org', "/nic/update?system=dyndns&hostname=unfo.ath.cx&myip=#{ip}%wildcard=OFF HTTP/1.0\r\nHost: members.dyndns.org\r\nAuthorization: Basic #{username}:#{password}\r\nUser-Agent: Mozilla/5.001 (Linux; U; Gentoo Linux; en-en) Gecko/25250101")
  #	213.169.6.162
  logmsg = update
  
  # Writing stuff into the logfile
else
  logmsg = "IP already up-to-date\n"
end

filename = 'ipupdate.log'
begin
  f = File.open(filename, 'a')
  f.puts Time.now.to_s + " | " + logmsg
rescue
  puts 'File error: ' + $!
ensure
  f.close unless f.nil?
end

