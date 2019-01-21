use std::str::from_utf8_owned;
use std::io::File;
 
fn main() {
    let path = Path::new("hw.txt");
    let mut hw_file = File::open(&path);
    let data = from_utf8_owned(hw_file.read_to_end());
    println!("{:s}", data);
}