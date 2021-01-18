<!-- Fixed navbar -->
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand"><i class="fas fa-lg">Demo</i></a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>index.html">Accueil</a></li>
                <li><a href="${content.rootpath}${config.archive_file}">Archives</a></li>
                <li><a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>about.html">A Propos</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>${config.feed_file}"><i class="fa fa-rss fa-lg"></i></a></li>
                <li><a href="https://github.com/cccp-education"><i class="fab fa-github fa-lg"></i></a></li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</div>
<div class="container">
