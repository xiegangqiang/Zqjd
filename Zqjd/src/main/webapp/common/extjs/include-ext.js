(function() {
    function getQueryParam(name) {
        var regex = RegExp('[?&]' + name + '=([^&]*)');

        var match = regex.exec(location.search) || regex.exec(path);
        return match && decodeURIComponent(match[1]);
    }

    function hasOption(opt, queryString) {
        var s = queryString || location.search;
        var re = new RegExp('(?:^|[&?])' + opt + '(?:[=]([^&]*))?(?:$|[&])', 'i');
        var m = re.exec(s);

        return m ? (m[1] === undefined || m[1] === '' ? true : m[1]) : false;
    }

    function getCookieValue(name){
        var cookies = document.cookie.split('; '),
            i = cookies.length,
            cookie, value;

        while(i--) {
           cookie = cookies[i].split('=');
           if (cookie[0] === name) {
               value = cookie[1];
           }
        }

        return value;
    }

    var scriptEls = document.getElementsByTagName('script'),
        path = scriptEls[scriptEls.length - 1].src,
        rtl = getQueryParam('rtl'),
        theme = getQueryParam('theme') || 'classic',
        includeCSS = !hasOption('nocss', path),
        neptune = (theme === 'classic'),
        repoDevMode = getCookieValue('ExtRepoDevMode'),
        suffix = [],
        i = 3,
        neptunePath;

    rtl = rtl && rtl.toString() === 'true';

    while (i--) {
        path = path.substring(0, path.lastIndexOf('/'));
    }
      
    if (theme /*&& theme !== 'classic'*/) {
        suffix.push(theme);
    }
    if (rtl) {
        suffix.push('rtl');
    } 
    var cssfix = suffix;
    suffix = (suffix.length) ? ('-' + suffix.join('-')) : '';
    if (includeCSS) {
    	document.write('<link rel="stylesheet" type="text/css" href="../app/resources/' + cssfix + '/' + cssfix + '-default.css">');
        document.write('<link rel="stylesheet" type="text/css" href="' + path + '/common/extjs/resources/ext-theme' + suffix + '/ext-theme' + suffix + '-all-debug.css"/>');
    }
    document.write('<script type="text/javascript" src="' + path + '/common/extjs/ext-all' + (rtl ? '-rtl' : '') + '.js"></script>');

})();