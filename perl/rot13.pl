sub rot13 {
### My old implementation.
	my @upper_chars = split(//, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	my @lower_chars = split(//, "abcdefghijklmnopqrstuvwxyz");
	my ($string) = @_;
	my $LOWER = ord('A');
	my $UPPER = ord('Z');
	my $lower = ord('a');
	my $upper = ord('z');
	my $tmp;
	my $result = "";
	foreach my $chr (split(//,$string)) {
		if ($LOWER <= ord($chr) && ord($chr) <= $UPPER) { #char is uppercase
			$tmp = ord($chr)+13;
			if ($tmp > $UPPER) {
				$tmp -= $UPPER-$LOWER+1;
			}
			$tmp -= $LOWER;
			$result .= $upper_chars[$tmp];
		} elsif ($lower <= ord($chr) && ord($chr) <= $upper) { #char is lowercase
			$tmp = ord($chr)+13;
			if ($tmp > $upper) {
				$tmp -= $upper-$lower+1;
			}
			$tmp -= $lower;
			$result .= $lower_chars[$tmp];
		} else {
			$result .= $chr;
		}
	}
	return $result;
}
sub tr_rot13 {
	my $val = shift;
	$val =~ tr/a-zA-Z/n-za-mN-ZA-M/;
	return $val;
}

