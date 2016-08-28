<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>微信后台管理系统</title>
    <link rel="stylesheet" type="text/css" href="../app/resources/default/css/icon.css">
    <link rel="stylesheet" type="text/css" href="../app/resources/default/css/style.css">
    <link rel="stylesheet" type="text/css" href="../common/extjs/ux/css/ItemSelector.css">
    <!-- GC -->

<style type="text/css">
* { font-size: 13px !important; font-family:微软雅黑; }

.x-btn-default-toolbar-large-icon-text-left .x-btn-inner  {
    font-family:微软雅黑  !important;
}

.x-btn-inner {
	font-family:微软雅黑  !important;
}

.x-form-item-label {
	font-family:微软雅黑  !important;
}

.x-form-item, .x-form-field {
	font-family:微软雅黑  !important;
}

.x-form-cb-label {
	font-family:微软雅黑  !important;
}

.x-tab-default .x-tab-inner {
	font-family:微软雅黑  !important;
}

.x-grid-group-title {
	font-family:微软雅黑  !important;
}

.feed-list {
    padding: 0 3px 0 3px;
}

.feed-list-item {
    margin-top: 3px;
    padding-left: 10px; 
    font-size: 13px;
    line-height: 32px;
    cursor: pointer;
    /* background: url(../app/resources/default/images/item.png) no-repeat 2px 3px; */
    border: 1px solid #fff;
}

.feed-list-item img {
	margin: 0px 5 -3px 0px;
}

/* .feed-list-item span {
	display: block;
	width: 95px;
	float:left;
	height:20px;
	text-align: center;
} */

.feed-list .x-item-selected {
    font-weight: bold; 
    color: #15428B;
    background-color: #DFE8F6;
    border: 1px dotted #A3BAE9;
}

.feed-list-item-hover {
    font-weight: bold; 
    color: #15428B;
    background-color: #DFE8F6;
    border: 1px dotted #A3BAE9;
}

.topic {
    padding: 2px 0 0 20px;
    background:transparent url(images/post.gif) no-repeat;
}

.x-rtl .topic {
    padding: 2px 20px 0 0;
    background:transparent url(images/post.gif) no-repeat top right;
}

.topic b {
    font-family: tahoma, verdana;
    display: block;
    color: #333;
}

</style>
    <script type="text/javascript" src="../common/extjs/include-ext.js"></script>
    <script type="text/javascript" src="../common/extjs/bootstrap.js"></script>
    <script type="text/javascript" src="../common/extjs/options-toolbar.js"></script>
    <script type="text/javascript" src="../common/extjs/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript">
    	Sys_Gabol_BasePath = "<%=basePath%>";
        function hasOption (name) {
            return window.location.search.indexOf(name) >= 0;
        }
        Ext.tip.QuickTipManager.init(); 
        Ext.Loader.setConfig({enabled: true});
        Ext.Loader.setPath('Ext.ux', '../common/extjs/ux');
        Ext.require([
            'Ext.grid.*',
            'Ext.data.*',
            'Ext.util.*',
            'Ext.Action',
            'Ext.tab.*',
            'Ext.button.*',
            'Ext.form.*',
            'Ext.ux.form.ItemSelector',
            'Ext.layout.container.Card',
            'Ext.layout.container.Border',
            'Ext.ux.ajax.SimManager',
            'Ext.ux.PreviewPlugin'
        ]);
        Ext.onReady(function(){
            /*if (hasOption('simjax')) {
                initAjaxSim();
            }*/
            var app = new SystemApp.App();
            
            Ext.Ajax.on('requestcomplete', function(conn,response,options){  
            	Ext.getCmp('app-header').items.get(1).items.get(0).setValue('00:59:59');
			    if(response.responseXML === null && response.getResponseHeader("sessionstatus")){  
			        window.location = "login.jsp";  
			    }  
			});  
        });
    </script>

    <script type="text/javascript" src="../app/layout/MainViewer.js"></script>
    
    <link rel="stylesheet" type="text/css" href="../common/extjs/ux/shared/example.css">
    <script charset="utf-8" src="/common/extjs/ux/shared/examples.js"></script>
    
    <link rel="stylesheet" href="/common/kindeditor/themes/default/default.css" />
	<script charset="utf-8" src="/common/kindeditor/kindeditor-min.js"></script>
	<script charset="utf-8" src="/common/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="/common/kindeditor/config.js"></script>
	<script charset="utf-8" src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.js"></script>
	<script charset="utf-8" src="/common/extjs/sysutils.js"></script>
	
</head>
<body>
</body>
</html>
