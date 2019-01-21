#!/usr/bin/perl
#Unix-based Perl/DBI/MySQL example
use DBI;
use strict;

#open connection to Access database
my $db = DBI->connect('dbi:mysql:unfo','username','hellogithub');

my $sqlstatement="SELECT * FROM url";
my $sth = $db->prepare($sqlstatement);
$sth->execute || 
      die "Could not execute SQL statement ... maybe invalid?";

#output database results
my @row;
my $column;
while (@row=$sth->fetchrow_array) {
	foreach $column (@row) {
		print "$column | ";
	}
	print "\n";
}
