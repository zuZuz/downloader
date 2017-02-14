Console downloader via http.

supported options:

    -n number of downloading threads
	-l rate limit in bytes/sec with suffixes k, m (k=1024, m=1024*1024)
	-f input file with links
	-o output folder

input file format:

	<http-link><space><filename>

example:

	http://example.com/archive.zip my_archive.zip
	http://example.com/image.jpg picture.jpg
	......

running example:

	java -jar utility.jar -n 5 -l 2000k -o output_folder -f links.txt

Readme file source: [Ecwid](https://github.com/Ecwid/new-job/blob/master/Console-downloader.md)
