#!/usr/bin/ruby

# This is the unit tests for LogParser

require 'test/unit'
require 'log_parser'

class TestLogParser < Test::Unit::TestCase

  def setup
    @testdata = { 
      'common' => '1.2.3.4 - dsmith [10/Oct/1999:21:15:05 +0500] "GET /index.html HTTP/1.0" 200 12',
      'combined' => '1.2.3.4 - dsmith [10/Oct/1999:21:15:05 +0500] "GET /index.html HTTP/1.0" 200 12 "http://www.ibm.com/" "Mozilla/4.05 [en] (WinNT; I)" "USERID=CustomerA;IMPID=01234"',
      'combined_without_cookies' => '1.2.3.4 - dsmith [10/Oct/1999:21:15:05 +0500] "GET /index.html HTTP/1.0" 200 12 "http://www.ibm.com/" "Mozilla/4.05 [en] (WinNT; I)"',
      'illegal_data' => '1998-11-19 22:48:39 206.175.82.5 - 208.201.133.173 GET /global/images/navlineboards.gif - 200 540 324 157 HTTP/1.0 Mozilla/4.0+(compatible;+MSIE+4.01;+Windows+95) USERID=CustomerA;+IMPID=01234 http://yourturn.rollingstone.com/webx?98@@webx1.html'
    }
  end

  def test_initialize
    assert_nil(LogParser.new.filename)
    assert_equal('name', LogParser.new('name').filename)
  end

  def test_check_format
    assert_equal(:common, LogParser.check_format(@testdata['common']))
    assert_equal(:combined, LogParser.check_format(@testdata['combined']))
    assert_equal(:combined, LogParser.check_format(@testdata['combined_without_cookies']))
    assert_equal(:unknown, LogParser.check_format(@testdata['illegal_data']))
    assert_equal(:unknown, LogParser.check_format(''))
  end

end
