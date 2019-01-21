<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
<xsl:output method="html"/>
<xsl:import href="lamerfest.xsl"/>

<xsl:template match="application">

	<span class="textHeading">Äänestys</span><br/>

	<div style="margin-left: 20px; padding: 10px">
		Lamerfesteillä järjestetään monenmoista
		kilpailua, joissa kaikilla osanottajilla
		on mahdollisuus vaikuttaa voittajaan
		äänestämällä. <b>Tunnukset jaetaan
		lauantaina iltapäivällä.</b>
	</div>

	<p>
		<xsl:apply-templates select="error"/>
		<xsl:apply-templates select="message"/>
	</p>

	<xsl:apply-templates select="data/*"/>

	<br/>

	<div align="right">
		Pavut keitettiin ajassa <xsl:value-of select="ejbProcessingTime"/> ms
	</div>

</xsl:template>

<xsl:template match="compos">

	<h4>Compot</h4>

	<div>
		<table cellpadding="2" cellspacing="1" class="listTable" width="100%">
			<tr valign="top">
			<th><b>nimi</b></th>
			<th><b>kuvaus</b></th>
			</tr>
		<xsl:if test="not(compo)">
			<tr>
				<td colspan="2">
					Ei compoja tällä hetkellä. Stay tuned.
				</td>
			</tr>
		</xsl:if>
		<xsl:for-each select="compo">
		<tr>
			<td><xsl:value-of select="compoName"/></td>
			<td><xsl:value-of select="compoDesc"/></td>
			<td>
				<xsl:text> </xsl:text>
				<xsl:choose>
				<xsl:when test="votingActive='true'">
					<a href="/voting?action=vote&amp;compoID={compoID}">Äänestä!</a>
					<xsl:text> </xsl:text>
				</xsl:when>
				<xsl:otherwise>
<a href="/voting?action=getEntries&amp;compoID={compoID}">Listaa tuotokset</a><xsl:text> </xsl:text>
				</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="browsable='true'">
					<a href="/voting?action=getResults&amp;compoID={compoID}"><b>Tulokset</b></a>
					<xsl:text> </xsl:text>
				</xsl:if>
			</td>
		</tr>
		</xsl:for-each>
		</table>
	</div>

</xsl:template>

<xsl:template match="entries">

	<xsl:variable select="@compoID" name="compoID"/>
	<xsl:variable select="@vote" name="vote"/>

	<h4>Tuotokset compossa #<xsl:value-of select="$compoID"/></h4>

	<div>
		<table cellpadding="2" cellspacing="1" class="listTable" width="100%">
			<tr valign="top">
			<th><b>nimi</b></th>
			<th><b>kuvaus</b></th>
			<th><b>tekijä</b></th>
			</tr>
		<xsl:for-each select="entry">
		<tr>
			<td><xsl:value-of select="entryName"/></td>
			<td><xsl:value-of select="entryDesc"/></td>
			<td><xsl:value-of select="entryAuthor"/></td>
			<xsl:if test="$vote='true'">
				<td><a href="/voting?action=castVote&amp;compoID={$compoID}&amp;entryID={entryID}">
					Äänestä tuotosta '<xsl:value-of select="entryName"/>'
				</a></td>
			</xsl:if>
		</tr>
		</xsl:for-each>
		</table>
	</div>	

</xsl:template>

<xsl:template match="results">

	<xsl:variable select="@compoID" name="compoID"/>

	<h4>Äänestystulokset, compo #<xsl:value-of select="$compoID"/></h4>

	<div>
		<table cellpadding="2" cellspacing="1" class="listTable" width="100%">
			<tr valign="top">
			<!--<th><b>sija</b></th>-->
			<th><b>nimi</b></th>
			<th><b>kuvaus</b></th>
			<th><b>tekijä</b></th>
			<th><b>ääniä</b></th>
			</tr>
		<xsl:for-each select="entry">
		<xsl:sort data-type="number" select="voteCount" order="descending"/>
		<tr>
			<!--<td><b>
				<xsl:variable select="voteCount" name="votes"/>
				<xsl:choose>
					<xsl:when test="../entry[0]/voteCount=$votes">1.</xsl:when>
					<xsl:otherwise><xsl:value-of select="position()"/>.</xsl:otherwise>
				</xsl:choose>
			</b></td>-->
			<td><xsl:value-of select="entryName"/></td>
			<td><xsl:value-of select="entryDesc"/></td>
			<td><xsl:value-of select="entryAuthor"/></td>
			<td><xsl:value-of select="voteCount"/></td>
		</tr>
		</xsl:for-each>
		</table>
	</div>	

</xsl:template>

<xsl:template match="error">
	<font color="red">
		<b>Crash! Boom! Bang!</b>
		<xsl:text> </xsl:text>
		<xsl:value-of select="."/><br/>
	</font>
</xsl:template>

<xsl:template match="message">
	<font color="blue">
		<xsl:value-of select="."/><br/>
	</font>
</xsl:template>

</xsl:stylesheet>

