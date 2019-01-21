<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
<xsl:output method="html"/>

<xsl:template match="/root">

	<html>

		<head><title>Lamerfest 2.0</title></head>
		<body>

			<xsl:apply-templates/>

		</body>

	</html>

</xsl:template>

<xsl:template match="error">
	<font color="red"><b>Error!</b> <xsl:value-of select="."/></font>
</xsl:template>

<xsl:template match="text">
	<xsl:value-of select="."/>
</xsl:template>

<xsl:template match="time">
	<table><tr><td bgcolor="black">
		<font color="white"><xsl:value-of select="."/></font>
	</td></tr></table>
</xsl:template>

<xsl:template match="bean">
	<blockquote>
		<b>Bean stuff.</b><br/>
		<xsl:apply-templates/>
	</blockquote>
</xsl:template>

</xsl:stylesheet>