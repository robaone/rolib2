<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<Report xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:noNamespaceSchemaLocation="../../../../../main/resources/com/robaone/dbase/XMLConnectionQuery.xsd">
			<ResultSet name="select">
				<Query>
					<PreparedStatement>select FirstName, LastName from Names<xsl:if
						test="count(//firstname) &gt; 0">
						<xsl:text> where FirstName = ?</xsl:text>
					</xsl:if></PreparedStatement>
					<xsl:if test="count(//firstname) &gt; 0">
						<Parameter name="firstname" type="string" />
					</xsl:if>
				</Query>
			</ResultSet>
			<ResultSet name="select_count">
				<Query>
					<PreparedStatement>select count(*) from Names<xsl:if
						test="count(//firstname) &gt; 0">
						where FirstName = ?
					</xsl:if></PreparedStatement>
					<xsl:if test="count(//firstname) &gt; 0">
						<Parameter name="firstname" type="string" />
					</xsl:if>
				</Query>
			</ResultSet>
		</Report>
	</xsl:template>
</xsl:stylesheet>