use std::io::BufferedReader;
use std::io::File;
use std::fmt;

struct WordCounter {
	p: uint,
	a: uint,
	i: uint,
	r: uint,
	s: uint,
}

fn wc_print(wc: WordCounter) -> String {
	format_args!(fmt::format, "WordCounter<{},{},{},{},{}>", 
		wc.p, 
		wc.a,
		wc.i,
		wc.r,
		wc.s)
}

fn main() {
	let path = Path::new("fastlove.txt");
	let mut file = BufferedReader::new(File::open(&path));
	let wordcounts = file.lines()
		.collect::<Vec<Result>>()
		.map(|line| 	match line { Ok(something) => something.as_slice(), Err(err) => "".as_slice() })
		.filter(|line| 	line.len() > 0 )
		.map(|sl| 		process_line(sl))
		.collect::<Vec<WordCounter>>();

	for wc in wordcounts.iter() {
		println!("{}", wc_print(*wc));
	}		
	// for line in file.lines() {
	// 	match line {
	// 		Ok(linestr) => println!("{}", wc_print(process_line(linestr.as_slice()))),
	// 		Err(err) => println!("Line failed, err={}", err),
	// 	}
	// }
}

fn process_line(line: &str) -> WordCounter {
	let mut wc = WordCounter { p:0,a:0,i:0,r:0,s:0 };
    for item in line.graphemes(true) {
    	match item {
    		"p" | "P" => wc.p += 1,
    		"a" | "A" => wc.a += 1,
    		"i" | "I" => wc.i += 1,
    		"r" | "R" => wc.r += 1,
    		"s" | "S" => wc.s += 1,
    		_ => { }
    	}
    }

    wc
}