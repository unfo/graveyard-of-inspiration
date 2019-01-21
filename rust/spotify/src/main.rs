extern crate hyper;
extern crate rustc_serialize;

use std::io::prelude::*;
use std::string::*;
use hyper::*;
use rustc_serialize::json;

#[derive(RustcDecodable, RustcEncodable)]
pub struct Artist {
	id:  String,
	name: String,
	uri: String,
}

impl ToString for Artist {
	fn to_string(&self) -> String {
		self.name
	}
}

#[derive(RustcDecodable, RustcEncodable)]
pub struct Album {
	id: String,
	name: String,
	uri: String,
	artists: Option<Vec<Artist>>
}

#[derive(RustcDecodable, RustcEncodable)]
pub struct Track {
	id: String,
	name: String,
	uri: String,
	artists: Vec<Artist>,
	album: Album
}

fn main() {
	let mut client = client::Client::new();

	let mut res = client.get("http://localhost:8000/tracks/3KDdL38cCcPI2JMcGZZKvu").send().unwrap();
	println!("Got response: {:?}", res.status);
	let mut contents = String::new();
	res.read_to_string(&mut contents);
	let track: Track = json::decode(&contents).unwrap();
	println!("Got track: {:?} from the album name {:?} by artist(s): {:?}",
			 track.name,
			 track.album.name,
			 track.artists.iter().map(|a| a.to_string() ).collect::<String>());
}
