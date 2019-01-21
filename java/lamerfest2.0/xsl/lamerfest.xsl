<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
<xsl:output method="html"/>

<xsl:variable select="document('menu.xml')/menu" name="menu"/>

<xsl:template match="lamerfest">

<html>
<head>
	<title>
		Lamerfest 2.0
		<xsl:if test="application/@title and application/@title != ''">
			 &gt;&gt; <xsl:value-of select="application/@title"/>
		</xsl:if>
	</title>
	<script language="JavaScript">
	<xsl:text disable-output-escaping="yes"><![CDATA[
	<!--

		if(navigator.userAgent.toLowerCase().indexOf("win") != -1)
			document.write('<l' + 'ink rel="stylesheet" type="text/css" href="/win.css">');
		else
			document.write('<l' + 'ink rel="stylesheet" type="text/css" href="/linux.css">');

	// -->
	]]></xsl:text>
	</script>
</head>

<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" bgcolor="#ffffff">

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td>
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
					<tr>
						<td>
							<img src="/kuvat/dot.gif" height="1" width="12"/>
							<script language="JavaScript">
							<xsl:comment>
								loppu = new Date();
								loppu.setDate(5);
								loppu.setMonth(10);
								loppu.setHours(13);
								loppu.setMinutes(0);
								togo = loppu - new Date();
								g = togo / 1000 / 60 / 60;
								h = Math.floor(g);
								m = Math.round((g - h) * 60);
								document.write(h + " h " + m + " min aikaa hakkaa CS:ää!");
							//</xsl:comment>
							</script>
						</td>
						<td align="right">
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
								<xsl:choose>
								<xsl:when test="user">
									<td>olet loggautunut sisään tunnuksella <b><xsl:value-of select="user/userID"/></b>&#160;</td>
				<xsl:if test="user/level='10'">
				<td align="right"
				id="logout" style="background-color:rgb(102,153,204)">
				&#160;<a href="/admin.xml" style="text-decoration:none">admin</a>&#160;
				</td>
				</xsl:if>
									<td align="right" id="logout">
										&#160;<a href="/logout">loggaudu ulos</a>&#160;
									</td>
								</xsl:when>
								<xsl:otherwise>
									<td align="right" id="logout">
										&#160;<a href="/login">loggaa sisään</a>&#160;
									</td>
								</xsl:otherwise>
								</xsl:choose>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#737373"><img src="/kuvat/dot.gif" height="1"/></td>
		</tr>
		<tr>
			<td>
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td><a href="/index"><img src="/kuvat/logo.gif" hspace="15" vspace="15" border="0"/></a></td>
						<td><img src="/kuvat/dot.gif" height="1" width="15"/></td>
						<td>
						<xsl:if test="application/@title and application/@title != ''">
					<span class="heading">&gt;&gt; <xsl:value-of select="application/@title"/></span>
						</xsl:if>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#737373"><img src="/kuvat/dot.gif" height="1"/></td>
		</tr>
		<tr>
			<td align="right">
				<table cellpadding="5"><tr><td>
				<xsl:for-each select="$menu/item">
					<a href="{@href}"><xsl:value-of select="."/></a>
					&#160;
				</xsl:for-each>
				</td></tr></table>
			</td>
		</tr>
		<tr>
			<td>

				<table cellpadding="0" cellspacing="0" border="0">
				<tr><td><img src="/kuvat/dot.gif" width="30" height="1"/></td><td>

					<!-- content här -->
					<xsl:apply-templates select="application"/>

				</td><td><img src="/kuvat/dot.gif" width="30" height="1"/></td></tr>
				</table>

			</td>
		</tr>
		<tr>
			<td>&#160;<br/>&#160;</td>
		</tr>
	</table>

</body>
</html>

</xsl:template>

</xsl:stylesheet>