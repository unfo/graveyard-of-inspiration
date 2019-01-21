<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html"/>

<xsl:template match="lamerfest">

	<xsl:apply-templates select="application"/>

</xsl:template>

<xsl:template match="application">

	<html>
		<head>
			<title>InfoTV</title>
			<style type="text/css">
			<xsl:comment>
				.heading {
					position: absolute;
					font-family: 'Trebuchet MS';
					font-size: 36pt;
					font-weight: bold;
					left: 100px;
					top: 150px;
					visibility: hidden;
				}
				.content {
					position: absolute;
					font-family: 'Trebuchet MS', Arial;
					font-size: 30pt;
					left: 150px;
					top: 220px;
					width: 690px;
					visibility: hidden;
				}
			</xsl:comment>
			</style>
			<script language="JavaScript">
			<xsl:comment>

				var items = 0;
				var currentItem = 0;
				var pauseBetweenItems = 7; // how many secs
				var chillTimeIfNoItems = 5; // again secs

				function init(){
					for(ii = 1; ; ii++){
						if(!document.all("heading" + ii)) break;
						items++;
					}
					if(items == 0){
						// chill for a while, then reload
						setTimeout("location.reload()", chillTimeIfNoItems * 1000);
	return;
					} else if(items &lt; 3)
	pauseBetweenItems = 12;
					cycle();
				}

				function cycle(){
					if(++currentItem > items){
						location.reload();
						return;
					}
					if(currentItem > 1) set(currentItem - 1, 0);
					set(currentItem, 1);
					setTimeout("cycle()", pauseBetweenItems * 1000);
				}

				function set(i, b){
					a = b ? "visible" : "hidden";
					document.all("heading" + i).style.visibility = a;
					document.all("content" + i).style.visibility = a;
				}

			// </xsl:comment>
			</script>
		</head>
		<body background="/kuvat/tausta4.jpg" scroll="no" onload="init()">

			<xsl:for-each select="items/item">
				<div id="heading{position()}" class="heading">
					<xsl:value-of select="heading"/>
				</div>
				<div id="content{position()}" class="content">
					<xsl:value-of select="content" disable-output-escaping="yes"/>
				</div>
			</xsl:for-each>

		</body>
	</html>

</xsl:template>

</xsl:stylesheet>