#!/bin/env ruby
#######################################################################
##
## This file generates an RSS-feed of new and changed files in 
## specified dir
## Copyright (c) 2005 Jan Wikholm, unfo-@irc.freenode.net, jw.fi
#######################################################################
# Adapted from:
# http://pragmaticprogrammer.com/downloads/commit2rss/commit2rss.rb.txt
# Whose Copyright (c) 2004 Dave Thomas, The Pragmatic Programmers
# Released under the same terms as Ruby. No warranty or support provided.
#######################################################################

program_start = Time.now

require 'rss/2.0'
require 'fileutils'
require 'logger'
require 'optparse'
require 'ostruct'

@log = Logger.new(File.join(Dir.pwd, 'logfile.log'))
@log.level = Logger::WARN
@log.info("BEGIN LOG");


module Rssgen


  def version
    'New files to RSS feed generator, version 1.0'
  end

  def check_options(options, from_commandline)
    # First let's declare the default values
    # Title of our RSS-feed
    options.rsstitle = "jw-test"
    # Max entries in the RSS file
    options.max_to_keep = 10
    # Path/Filename to store the feed
    options.drop_dir = File.join(Dir.pwd, 'rss')
    options.drop_file = options.rsstitle + ".rss"
    # Where we create our RSS files
    options.rssfile = File.join(options.drop_dir, options.drop_file)
    # Filename to read/write the file listing
    options.filelist = 'innards.lst'
    # Directory to use as target for scanning
    options.targetdir = File.join(Dir.pwd, 'ftp-site')
    # URL for the rss
    options.rssurl = "http://jw.fi/rss/#{options.rsstitle}.rss"

    # Parse thru possible command-line options
    opts = OptionParser.new do |opts|
      opts.banner = "Usage: rss-generator.rb [options]"
      opts.separator ""
      opts.separator "Specific options:"
      
      
      # Optional argument.
      opts.on("-t", "--title 'Title of RSS feed'", 
              "Specify the title of your RSS feed") do |title|
        options.rsstitle = title
      end  
      # Optional argument; Cast max to decimal integer 
      opts.on("-m", "--max <positive number>", 
              OptionParser::DecimalInteger, 
              "Specify how many old file lists to keep") do |max|
        options.max_to_keep = max if max > 0
      end
      # Optional argument.
      opts.on("-dd", "--drop-dir /path/to/dropdir", "Default is ./rss/") do |dd|
        options.drop_dir = dd
      end
      # Optional argument; multi-line description.
      opts.on("-f", "--rss-file <filename>", 
              "This will be concatenated to the rss drop_dir.",
              "Default is (rsstitle + .rss). eg. 'unfo.rss'") do |name|
        options.drop_file = name
      end
      
      # Optional argument.
      opts.on("-l", "--list /path/to/file.lst",
              "Specify where to read and write the file list") do |list|
        options.filelist = list
      end
      
      # Optional argument.
      opts.on("-T", "--target /path/to/scan/", 
              "Specify which dir to scan for new files") do |dir|
        options.targetdir = dir
      end
      
      # Optional argument.
      opts.on("-u", "--url <url>", "Assign <url> as the RSS link") do |url|
        options.rssurl = url
      end
    
      opts.separator ""
      opts.separator "Common options:"
    
      # Print out help
      opts.on_tail("-h", "--help", "Show this message") do
        puts opts
        exit
      end
      
      # Print version
      opts.on_tail("--version", "Show version") do
        #puts OptionParser::Version.join('.')
        puts version()
        exit
      end
    end #end opts
    opts.parse!(from_commandline)
    return options
  end #end check_options
  
  # Read in existing rss (we'll write it out again to the new file)
  def read_rss(filename)
    @log.warn("read_rss called with " + filename)
    begin
      existing_data = RSS::Parser.parse(File.read(filename), false)
    rescue
      existing_data = RSS::Rss.new("2.0")
    end
    return existing_data
  end #read_rss
  
    
  def write_file(dir, filename, content)
    fullname = File.join(dir, filename)
    if File.exists?(dir)
      if File.writable?(dir)
        begin #try to write the file
          file = File.new(fullname, "w") 
          file.puts(content.to_s)
        rescue SystemCallError
          $stderr.print "Writing file failed: " + $!
          file.close
          File.delete(fullname)
          raise
        ensure
          file.close unless file.nil?
        end # stop trying
      else # dir not writable
        raise "Directory %s is not writable" % dir
      end # if dir writable
    else # dir does not exist
      begin #try to create the dir
        newDir = Dir.mkdir(dir)
        begin #try to write the file
          file = File.new(fullname, "w") 
          file.puts(content.to_s)
        rescue SystemCallError
          $stderr.print "Writing file failed: " + $!
          file.close
          File.delete(fullname)
          raise
        ensure
          file.close unless file.nil?
        end # stop trying
      rescue SystemCallError
        $stderr.print "Creating dir failed: " + $!
        raise
      end # stop trying to mkdir
    end
  end # write_file

  alias_method :write_rss,  :write_file
  alias_method :write_list, :write_file


  
  # Read in the latest file list
  def read_list(filename)
    @log.info("read_list called with " + filename)
    if File.exists?(filename)
      oldlist = IO.readlines(filename)
      return oldlist.flatten.join(" ")
    else
      return " "
    end #File.exists?
  end
  
  # pretty print filesize
  # Original from 
  # http://blade.nagaokaut.ac.jp/cgi-bin/scat.rb/ruby/ruby-talk/24247
  def commafy(number)
    # Reverse the digits that make up the number to ease processing.
    number = number.to_s.reverse
    # Add commas in the appropriate places.
    number.gsub!(/(\d\d\d)(?=\d)(?!\d*\.)/) do |match|
      $1 + ' '
    end
    # Reverse the result back to the proper form.
    number = number.to_s.reverse
  end
  
  # compare old listing to the current situation
  def checkDir(aDir, oldlist)
    @log.info("checkDir called with " << aDir << " and " << oldlist.to_s)
    oldlist = " " if oldlist.nil?
    result = ""
    newlist = ""
    Dir.foreach(aDir) do |file|
      full_filename = File.join(aDir,file)
      @log.info("\tProcessing %s" % file)
      unless (file =~ /^\./) # We don't want . or .. or .svn etc
        @log.info("%s didn't start with ." % file)
        @log.info("Filetype: %s" % File.ftype(File.join(aDir, file)))
        if File.directory?(full_filename)
          result_list = checkDir(full_filename, oldlist) 
          # add new stuff from the "inner" checkDir to our result
          result << result_list[0]         unless result_list[0].empty? 
          # add all files from to the filelist too
          newlist << result_list[1] << "\n" unless result_list[1].empty? 
          @log.info("result_list[1].empty? => %s" % result_list[1].empty?)
        end
        unless oldlist.include?(file)
        result += <<EOM 
<tr>
  <td>#{full_filename}</td>
  <td>#{commafy(File.size(full_filename))} B</td>
</tr>
EOM
        end
        @log.info("\tResult: %s (file=%s)" % [result, file])
        newlist << full_filename << "\n" # always update the dirlist
      end #unless
    end #Dir.foreach
    @log.info("Going to return: '%s' and '%s'" % [result, newlist.strip])
    return [result, newlist.strip]
  end #checkDir
  
  def surroundings_and_content(content)
    fullcontent = <<EOD
<b>New files:</b><br/>
<table>
  #{content}
</table><br />
EOD
    return fullcontent
  end #surroundings_and_content
  
  def create_rss_and_chan(title, url)
    rss = RSS::Rss.new("2.0")
    chan = RSS::Rss::Channel.new
    chan.title = chan.description = title
    chan.link = url
    rss.channel = chan
    return [rss, chan]
  end #create_rss_and_chan
  
  def create_rss_item(content)
    item = RSS::Rss::Channel::Item.new
    item.title =  "New items at " + Time.now.strftime("%b %d, %H:%M:%S")
    item.pubDate = Time.now
    item.description = content
    return item
  end
end # end module


def main
  include Rssgen
  # This for parsing possible command-line options
  options = check_options(OpenStruct.new, ARGV)
  
  # read the old list in
  oldlist = read_list(options.filelist)
  
  # recursively check all subdirs of target.
  result, newlist = checkDir(options.targetdir, oldlist)
  @log.info("Got from checkDir: result=[%s] and newlist=[%s]" % [result, newlist] )

  unless result.strip.empty?
    # Print a lil' something before our filelist
    desc = surroundings_and_content(result)
    
    # Create a new RSS-feed and channel
    rss, chan = create_rss_and_chan(options.rsstitle, options.rssurl)
    
    # first up is of course our newest files
    item = create_rss_item(desc)
    chan.items << item
    
    # Then up to n-1 items from old data
    existing_data = read_rss(File.join(options.drop_dir, options.drop_file))
    chan.items.concat existing_data.items[0, options.max_to_keep-1]
    
      
    # write the whole shabang to a the same file
    write_rss(options.drop_dir, options.drop_file, rss)
    
    # write a new list for next time
    write_list(Dir.pwd, options.filelist, newlist)
  else # result is empty
    puts "Nothing new"
  end #unless
end

# actually execute the program
main
@log.warn("Execution finished. it took: %f seconds" % (Time.now - program_start).to_f)
