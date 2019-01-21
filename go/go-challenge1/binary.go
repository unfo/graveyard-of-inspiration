package main

import (
	"bytes"
	"encoding/binary"
	"fmt"
	"os"
	"log"
)
const (
	HEADER = "SPLICE"
	HEADER_BYTES = 13
	LENGTH_BYTES = 1
	VERSION_BYTES = 32
	TEMPO_BYTES = 4
	TRACK_ID_BYTES = 1
	TRACK_NAME_BYTES = 4
	PATTERN_BYTES = 16
)

type Track struct {
	Id 				uint
	NameLength 		uint
	Name 			string
	Pattern			[PATTERN_BYTES]byte
}
type PatternFile struct {
	SpliceHeader 	string
	Length 			uint
    Version 		string
    Tempo 			float32
    Tracks 			[]Track
}


func main() {
	if len(os.Args) < 2 {
		log.Fatal("Use: COMMAND <file>")
	}

	file, err := os.Open(os.Args[1])
	if err != nil {
		log.Fatal(err)
	}

	pf := &PatternFile{}
	header := make([]byte, HEADER_BYTES)
	_ , err = file.Read(header)
	
	if err != nil {
		log.Fatal(err)
	}

	pf.SpliceHeader = string(header[0:len(HEADER)])
	if pf.SpliceHeader != HEADER {
		log.Fatal("Did not find header!")
	}

	clength := make([]byte, LENGTH_BYTES)
	_,err = file.Read(clength)
	if err != nil {
			log.Fatal(err)
	}
	fmt.Printf("Got %d as length\n", clength[0])
	pf.Length = uint(clength[0])

	content := make([]byte, pf.Length)
	_,err = file.Read(content)
	if err != nil {
			log.Fatal(err)
	}
	ptr := uint(0)
	
	pf.Tempo = readTempo(content[ptr:(ptr+TEMPO_BYTES)])
	ptr += TEMPO_BYTES
	fmt.Printf("%v\n", pf)

	
	fmt.Printf("Done adding tracks\n%v", pf)
	file.Close()

}

func readTempo(encoded []byte) float32 {
	var tempo float32
	buf := bytes.NewReader(encoded)
	err := binary.Read(buf, binary.LittleEndian, &tempo)
	if err != nil {
		fmt.Println("binary.Read failed:", err)
		tempo = 0
	}

	return tempo
}