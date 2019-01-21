<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
<xsl:output method="html"/>
<xsl:import href="lamerfest.xsl"/>

<!--

	This is what VotingManagerServlet produces:

	<message>something good happened</message>
	<error>something bad occured</error>

	<ejbProcessingTime>150</ejbProcessingTime>

-->

<xsl:template match="application">

	<span class="textHeading">Voting manager</span><br/>

	Tältä sivulta voit hallita compoja, lisätä/poistaa
	entryjä yms. yms.
	<br/><br/>
	<a href="votingmanager?">Compot</a>

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
			<th>id</th>
			<th>nimi</th>
			<th>kuvaus</th>
			<th>äänestys<br/>käynnissä</th>
			<th>tulokset<br/>julki</th>
			</tr>
		<xsl:for-each select="compo">
			<xsl:sort data-type="number" select="compoID"/>
			<form method="POST" action="/votingmanager">
			<input type="hidden" name="compoID" value="{compoID}"/>
			<tr valign="top">
			<td>
				<xsl:value-of select="compoID"/>
			</td>
			<td>
				<input type="text" size="10" name="compoName" value="{compoName}"/>
			</td>
			<td>
	<textarea cols="40" rows="4" name="compoDesc"><xsl:value-of select="compoDesc"/></textarea>
			</td>
			<td>
				<input type="checkbox" name="votingActive" value="set">
					<xsl:if test="votingActive='true'">
						<xsl:attribute name="checked">true</xsl:attribute>
					</xsl:if>
				</input>
			</td>
			<td>
				<input type="checkbox" name="browsable" value="set">
					<xsl:if test="browsable='true'">
						<xsl:attribute name="checked">true</xsl:attribute>
					</xsl:if>
				</input>
			</td>
			</tr>
			<tr>
			<td align="right" colspan="5">
				<a href="/votingmanager?action=getEntries&amp;compoID={compoID}"><b>LISTAA ENTRYT</b></a>&#160;
				<a href="/votingmanager?action=removeCompo&amp;compoID={compoID}" onclick="return confirm('oikeesti?')">POISTA</a>&#160;
				<a href="/votingmanager?action=resetVotes&amp;compoID={compoID}">NOLLAA ÄÄNESTYSTILANNE</a>&#160;
				<input type="submit" name="action" value="update compo"/>
			</td>
			</tr>
			</form>
		</xsl:for-each>
		</table>
	</div>

	<h4>Lisää compo</h4>

	<div>
	<form method="POST" action="/votingmanager">
		<input type="hidden" name="action" value="addCompo"/>
		<b>nimi</b><br/>
		<input type="text" size="12" name="compoName"/><br/>
		<b>kuvaus</b><br/>
		<textarea cols="40" rows="4" name="compoDesc"></textarea><br/>
		<br/>
		<input type="submit" value="Lisää &gt;&gt;"/>
	</form>
	</div>

</xsl:template>

<xsl:template match="entries">

	<xsl:variable name="compoID" select="@compoID"/>

	<h4>Entryt compossa #<xsl:value-of select="$compoID"/></h4>

	<div>
		<table cellpadding="2" cellspacing="1" class="listTable" width="100%">
			<tr valign="top">
			<th>id</th>
			<th>nimi</th>
			<th>kuvaus</th>
			<th>tekijä</th>
			</tr>
		<xsl:for-each select="entry">
			<xsl:sort data-type="number" select="entryID"/>
			<form method="POST" action="/votingmanager">
			<input type="hidden" name="compoID" value="{$compoID}"/>
			<input type="hidden" name="entryID" value="{entryID}"/>
			<tr valign="top">
			<td>
				<xsl:value-of select="entryID"/>
			</td>
			<td>
				<input type="text" size="10" name="entryName" value="{entryName}"/>
			</td>
			<td>
				<textarea cols="40" rows="4" name="entryDesc"><xsl:value-of select="entryDesc"/></textarea>
			</td>
			<td>
				<input type="text" size="10" name="entryAuthor" value="{entryAuthor}"/>
			</td>
			</tr>
			<tr>
			<td align="right" colspan="4">
				<a href="/votingmanager?action=removeEntry&amp;compoID={$compoID}&amp;entryID={entryID}">
					POISTA</a>&#160;
				<input type="submit" name="action" value="update entry"/>
			</td>
			</tr>
			</form>
		</xsl:for-each>
		</table>
	</div>

	<h4>Lisää entry compoon</h4>

	<div>
	<form method="POST" action="/votingmanager">
		<input type="hidden" name="action" value="addEntry"/>
		<input type="hidden" name="compoID" value="{$compoID}"/>
		<b>nimi</b><br/>
		<input type="text" size="12" name="entryName"/><br/>
		<b>kuvaus</b><br/>
		<textarea name="entryDesc" cols="40" rows="4"></textarea><br/>
		<b>tekijä</b><br/>
		<input type="text" size="12" name="entryAuthor"/><br/>
		<br/>
		<input type="submit" value="Lisää &gt;&gt;"/>
	</form>
	</div>

</xsl:template>

<xsl:template match="error">
	<font color="red">
		<b>Error!</b>
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