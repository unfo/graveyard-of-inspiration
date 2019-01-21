package drum

import (
	"bytes"
	"encoding/binary"
	"fmt"
	"log"
	"os"
	"strings"
)

const (
	HEADER           = "SPLICE"
	HEADER_BYTES     = 13
	LENGTH_BYTES     = 1
	VERSION_BYTES    = 32
	TEMPO_BYTES      = 4
	TRACK_ID_BYTES   = 1
	TRACK_NAME_BYTES = 4
	PATTERN_BYTES    = 16
)

// DecodeFile decodes the drum machine file found at the provided path
// and returns a pointer to a parsed pattern which is the entry point to the
// rest of the data.
func DecodeFile(path string) (*Pattern, error) {
	p := &Pattern{}
	var err error
	_, err = p.ReadRawContents(path)
	if err != nil {
		log.Fatal("Unable to read file", err)
		return p, err
	}

	_, err = p.ParseVersion()
	if err != nil {
		log.Fatal("Unable to parse version", err)
		return p, err
	}

	_, err = p.ParseTempo()
	if err != nil {
		log.Fatal("Unable to tempo", err)
		return p, err
	}

	_, err = p.ParseTracks()
	if err != nil {
		log.Fatal("Unable to parse tracks", err)
	}

	return p, err
}

// Pattern is the high level representation of the
// drum pattern contained in a .splice file.
type Pattern struct {
	RawContents  []byte
	ContentIndex uint
	Length       uint
	Version      string
	Tempo        float32
	Tracks       []Track
}

type Track struct {
	Id         uint
	NameLength uint
	Name       string
	Pattern    [16]byte
}

type ParseError struct {
	Field string
}

func (e *ParseError) Error() string {
	return fmt.Sprintf("Failure at parsing %s", e.Field)
}

func (p *Pattern) ReadRawContents(path string) ([]byte, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}

	header := make([]byte, HEADER_BYTES)
	_, err = file.Read(header)

	if err != nil {
		file.Close()
		return nil, err
	}

	if string(header[0:len(HEADER)]) != HEADER {
		file.Close()
		return nil, &ParseError{"Header"}
	}

	clength := make([]byte, LENGTH_BYTES)
	_, err = file.Read(clength)
	if err != nil {
		file.Close()
		return nil, &ParseError{"Length"}
	}
	p.Length = uint(clength[0])

	content := make([]byte, p.Length)
	readBytes, err := file.Read(content)
	if err != nil || readBytes < int(p.Length) {
		file.Close()
		return nil, &ParseError{"RawContents"}
	}

	file.Close()
	p.RawContents = content
	p.ContentIndex = 0
	return p.RawContents, nil
}

func (p *Pattern) ParseVersion() (string, error) {
	from, to := p.ContentIndex, p.ContentIndex+VERSION_BYTES
	fmt.Printf("Reading version, from=%d, to=%d, length=%d", from, to, len(p.RawContents))
	p.Version = strings.TrimRight(string(p.RawContents[from:to]), string(0x00))
	p.ContentIndex = to

	return p.Version, nil
}

func (p *Pattern) ParseTempo() (float32, error) {
	from, to := p.ContentIndex, p.ContentIndex+TEMPO_BYTES

	var tempo float32
	buf := bytes.NewReader(p.RawContents[from:to])
	err := binary.Read(buf, binary.LittleEndian, &tempo)
	if err != nil {
		return 0, &ParseError{"Tempo"}
	}

	p.Tempo = tempo
	p.ContentIndex = to

	return tempo, nil
}

func (p *Pattern) ParseTracks() ([]Track, error) {
	from, to := p.ContentIndex, p.Length
	for from < to {
		track := &Track{}
		forward, err := track.ParseTrack(p.RawContents[from:to])
		if err != nil {
			return nil, err
		}

		from += forward
		p.Tracks = append(p.Tracks, *track)
	}

	return p.Tracks, nil
}

func (p *Pattern) String() string {
	tempoFormat := fmt.Sprintf("%.0f", p.Tempo)
	if float32(int(p.Tempo)) < p.Tempo {
		tempoFormat = fmt.Sprintf("%.1f", p.Tempo)
	}
	str := fmt.Sprintf("Saved with HW Version: %s\nTempo: %s\n", p.Version, tempoFormat)
	for _, track := range p.Tracks {
		str += track.String() + "\n"
	}
	return str
}

func (t *Track) String() string {
	//(0) kick    |x---|x---|x---|x---|
	return fmt.Sprintf("(%d) %s\t%s", t.Id, t.Name, t.Bars())
}

func (t *Track) Bars() string {
	str := ""
	for i := uint(0); i < 16; i++ {
		if i%4 == 0 {
			str += "|"
		}
		if t.Pattern[i] == 1 {
			str += "x"
		} else {
			str += "-"
		}
	}
	str += "|"

	return str
}

func (t *Track) ParseTrack(content []byte) (uint, error) {
	index := uint(0)

	t.Id = uint(content[index])
	index++

	index += 3 // empty bytes -- or maybe they are used for a bigger number

	t.NameLength = uint(content[index])
	index++

	nameEnd := index + t.NameLength
	t.Name = string(content[index:nameEnd])
	index = nameEnd

	for i := uint(0); i < PATTERN_BYTES; i++ {
		t.Pattern[i] = content[index]
		index++
	}

	return index, nil
}
