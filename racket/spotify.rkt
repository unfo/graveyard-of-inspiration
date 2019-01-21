#lang racket
(require racket/file)
(require json)
(require racket/string)
(require net/url net/uri-codec)
(require racket/port)
(require racket/cmdline)


;(define spotify-example 
;  (file->string "/Users/jw/fun/racket/spotify-artist.json" #:mode 'text))
;
;(define spotify-urls
;  (file->lines "/Users/jw/fun/racket/spotify-urls.txt" #:mode 'text))
;(define irc-line "10:04 <@xxxxx> vähä domateckii näin ma-aamuks http://open.spotify.com/track/5my8vEUQp8DMSX01zFoX9d")

(define (follow-irc input-path output-path)
  (define in (open-input-file input-path #:mode 'text))
  (define (loop)
    (spotify-handler in output-path)
    (sleep 5)
    (loop))
  (define t (thread loop))
  (lambda ()
    (kill-thread t)
    (close-input-port in)))

(define (spotify-handler input output)
  (define t (thread
             (lambda ()
               (handle-line (read-line input) output))))
  (thread (lambda ()
            (sleep 10)
            (kill-thread t))))

(define (handle-line line-or-eof output)
  (if (and (string? line-or-eof) (is-spotify-link? line-or-eof))
      (write-to-channel output (get-info line-or-eof))
      (void)))

(define (write-to-channel output-path text) (display-lines-to-file (list text) output-path
                                                                   #:mode 'text #:exists 'append))
(define (spotify-read jsonresult)
  (string->jsexpr jsonresult))


(define (is-spotify-link? txt)
  (regexp-match? #rx"spotify(\\.com/|:)(track|album|artist)(/|:)([A-Za-z0-9]+)" txt))

(define api-base "https://api.spotify.com/v1/")
;;(define api-base "http://localhost:8000/")
(define (api-url txt)
  (let ([sp-uri (parse-uri txt)])
    (if (spotifyuri? sp-uri)
        (string-join (list api-base (spotifyuri-target sp-uri) "s/" (spotifyuri-id sp-uri)) "")
        "")))

(struct spotifyuri (utype target id)
  #:transparent)

(define (parse-uri uri)
  (if (is-spotify-link? uri)
      (let* ([match (regexp-match #rx"spotify(\\.com/|:)(track|album|artist)(/|:)([A-Za-z0-9]+)" uri)]
             [uritype (list-ref match 1)]
             [type (list-ref match 2)]
             [id (last match)])
        (spotifyuri 
         (if (string=? uritype ":") 'uri 'http) 
         type
         id))
      #f))


(define (urlopen url)
  (if (regexp-match? #rx"^http" url)
      (call/input-url
       (string->url url)
       (curry get-pure-port #:redirections 5)
       port->string)
      ""))

(define (artists spotify-json)
  (string-join 
   (map 
    (lambda (artist) 
      (hash-ref artist 'name)) 
    (hash-ref spotify-json 'artists (list (hash 'name "Unknown artist"))))
   ", "))

(define (album-name spotify-json)
  (cond ((is-album? spotify-json) (hash-ref spotify-json 'name "Unknown album"))
        ((is-track? spotify-json) (album-name (hash-ref spotify-json 'album)))))

(define (track-name spotify-json)
  (if (is-track? spotify-json)
      (hash-ref spotify-json 'name)
      ""))

(define (artist-and-album res)
  (string-append (artists res) " -- " (album-name res) " [album]"))

(define (artist-and-album-and-track res)
  (string-append (artists res) " -- " (album-name res) " -- " (track-name res)))



(define (pretty-print-info spotify-json)
  (cond ((is-album? spotify-json) (artist-and-album spotify-json))
        ((is-track? spotify-json) (artist-and-album-and-track spotify-json) )
        ((is-artist? spotify-json) (hash-ref spotify-json 'name))))

(define (is-album? res) (string=? "album" (hash-ref res 'type)))
(define (is-track? res) (string=? "track" (hash-ref res 'type)))
(define (is-artist? res) (string=? "artist" (hash-ref res 'type)))

(define (get-info uri)
  (let* ([spuri (parse-uri uri)]
         [apiurl (api-url uri)]
         [response (urlopen apiurl)]
         [json (spotify-read response)])
    (string-append (pretty-print-info json) " // " (if (is-http? spuri) (display-spotify-uri spuri) (display-http-uri spuri)))))

(define (is-http? uri)
  (eq? (spotifyuri-utype uri) 'http))

(define (display-spotify-uri uri)
  (string-append "spotify:" (spotifyuri-target uri) ":" (spotifyuri-id uri)))

(define (display-http-uri uri)
  (string-append "https://open.spotify.com/" (spotifyuri-target uri) "/" (spotifyuri-id uri)))


(define (get-example-artists)
  (let* ([http-urls (filter (lambda (url) (and (is-spotify-link? url) (regexp-match? #rx"^http" url))) spotify-urls)]
         [api-urls (map api-url http-urls)])
    (map (lambda (url) (string-join (list url " => " (pretty-print (spotify-read (urlopen url)))) "")) api-urls)))

;(get-info "https://open.spotify.com/track/6LrCe0pG6ZpJRjmm1l5oHs")
;(map get-info spotify-urls)
;(follow-irc "irkkiloki")
;(display (vector-length (current-command-line-arguments)))

(if (= (vector-length (current-command-line-arguments)) 2)
    (let ([in-chan (vector-ref (current-command-line-arguments) 0)]
          [out-chan (vector-ref (current-command-line-arguments) 1)])
      (follow-irc in-chan out-chan))
    (display "use: prog in out"))
