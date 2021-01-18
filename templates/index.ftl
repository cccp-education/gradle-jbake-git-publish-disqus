<#include "header.ftl">
	
	<#include "menu.ftl">

	<div class="page-header"/>
	<#list posts as post>
  		<#if (post.status == "published")>
  			<a href="${post.uri}">
  			<h1><#escape x as x?xml>${post.title}</#escape></h1></a>
  			<p>${post.date?string("dd MMMM yyyy")}</p>
  			<p>${post.body}</p>
  		</#if>
  	</#list>
	
	<hr/>
	
	<p>Les anciens post sont dipsonible içi : <a href="${content.rootpath}${config.archive_file}">archive</a>.</p>

<#include "footer.ftl">