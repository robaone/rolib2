<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" omit-xml-declaration="yes"/>
	<xsl:template match="/">
		<xsl:value-of select="//name"/>
	</xsl:template>
</xsl:stylesheet>