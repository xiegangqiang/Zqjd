Ext.define('SystemApp.View.Website', {
    extend: 'Ext.panel.Panel',
    xtype: 'website',
    
    initComponent: function() {
    	
    	Ext.apply(this, {
            layout: 'border',
            items: [this.createView()]
        });
    	
    	this.loadDataToView();
        this.callParent(arguments);
    },
    
    
    createView: function() {

    	this.homepanel = null, this.listpanel = null, this.detailpanel = null;
    	
    	this.homestore = Ext.create('Ext.data.Store', {
    	    fields: ['img', 'name', 'id'],
    	    data: []
    	});
    	
    	this.liststore = Ext.create('Ext.data.Store', {
    	    fields: ['img', 'name', 'id'],
    	    data: []
    	});
    	
    	this.detailstore = Ext.create('Ext.data.Store', {
    	    fields: ['img', 'name', 'id'],
    	    data: []
    	});
    	
    	var imageTpl = new Ext.XTemplate(
    		    '<tpl for=".">',
    		        '<div style="margin-bottom: 10px;" class="thumb-wrap">',
    		          '<img src="{img}" />',
    		          '<span>{name}</span>',
    		        '</div>',
    		    '</tpl>'
    		);
    	
    	this.tabpanel = Ext.create('Ext.tab.Panel', {
    		region: 'center',
    	    items: [{
    	        title: '图文详细页模版风格',
    	        layout: 'fit',
    	        items: [this.detailpanel = Ext.create('Ext.view.View', {
    	        	xtype: 'dataview',
    	        	store: this.detailstore,
    	     	    tpl: imageTpl,
    	     	    itemSelector: 'div.thumb-wrap',
	   	     	    cls: 'webcls',
	   	     	    name: 'detailtmp',
	   	     	    autoScroll: true,
	   	     	    padding: '20 30',
	   	     	    trackOver: true,
	   	     	    autoScroll: true,
	   	     	    listeners: {
		   	     	    scope: this,
		     	    	containerclick: function(){
		     	    		return false;
		     	    	},
		     	    	refresh: this.loadWxData,
		     	    	itemclick: this.selectModel
	   	     	    }
    	        })],
    	        tbar: [{ 
    	        	xtype: 'button', 
    	        	text: '预览模版',
    	        	scope: this,
    	        	handler: this.scanWebsiteByDetail
    	        }]
    	    }, {
    	        title: '栏目首页模版风格',
    	        layout: 'fit',
    	        items: [this.homepanel = Ext.create('Ext.view.View', {
    	        	xtype: 'dataview',
    	        	store: this.homestore,
    	     	    tpl: imageTpl,
    	     	    itemSelector: 'div.thumb-wrap',
    	     	    cls: 'webcls',
    	     	    autoScroll: true,
    	     	    padding: '20 30',
    	     	    name: 'hometmp',
    	     	    multiSelect : true,
    	     	    autoScroll: true,
    	     	    trackOver: true,
    	     	    listeners: {
    	     	    	scope: this,
    	     	    	containerclick: function(){
    	     	    		return false;
    	     	    	},
    	     	    	refresh: this.loadWxData,
		     	    	itemclick: this.selectModel
    	     	    }
    	        })],
    	        tbar: [{ 
    	        	xtype: 'button', 
    	        	text: '预览模版',
    	        	scope: this,
    	        	handler: this.scanWebsiteByHome
    	        }]
    	    }, {
    	        title: '图文列表模版风格',
    	        layout: 'fit',
    	        items: [this.listpanel = Ext.create('Ext.view.View', {
    	        	xtype: 'dataview',
    	        	store: this.liststore,
    	     	    tpl: imageTpl,
    	     	    itemSelector: 'div.thumb-wrap',
	   	     	    cls: 'webcls',
	   	     	    autoScroll: true,
	   	     	    padding: '20 30',
	   	     	    name: 'listtmp',
	   	     	    trackOver: true,
	   	     	    autoScroll: true,
	   	     	    listeners: {
		   	     	    scope: this,
		     	    	containerclick: function(){
		     	    		return false;
		     	    	},
		     	    	refresh: this.loadWxData,
		     	    	itemclick: this.selectModel
	   	     	    }
    	        })],
    	        tbar: [{ 
    	        	xtype: 'button', 
    	        	text: '预览模版',
    	        	scope: this,
    	        	handler: this.scanWebsiteByList
    	        }]
    	    }]
    	});
    	return this.tabpanel;
    },
    
    loadDataToView: function() {
    	var homepanel = this.homepanel;
    	var homestore = this.homestore;
    	var liststore = this.liststore;
    	var detailstore = this.detailstore;
    	Ext.Ajax.request({
    		async: false,
    	    url: '/admin/website.do?list',
    	    success: function(response){
    	    	var res = Ext.decode(response.responseText);
    	    	for (var i = 0; i < res.length; i++) {
    	    		if (res[i].type == 1) {
    	    			homestore.add(res[i]);
    	    		} else if (res[i].type == 2) {
    	    			liststore.add(res[i]);
    	    		} else if (res[i].type == 3) {
    	    			detailstore.add(res[i]);
    	    		}
    	    	}
    	    },
    	    failure: function(response) {
    	    	Ext.Msg.alert('提示', '操作异常');
    	    }
    	});
    	
    },
    
    loadWxData: function() {
    	if (this.data == null || this.data ==undefined) {
    		var res = null;
    		Ext.Ajax.request({
        		async: false,
        	    url: '/admin/website.do?info',
        	    success: function(response){
        	    	res = Ext.decode(response.responseText);
        	    },
        	    failure: function(response) {
        	    	Ext.Msg.alert('提示', '操作异常');
        	    }
        	});
    		this.data = res;
    	}
    	
    	this.weixinId = this.data.wx;
    	
    	if (this.data.hometmp != null && this.data.hometmp != '') {
    		var node = this.homestore.getById(this.data.hometmp);
    		this.homepanel.getSelectionModel().select(node);
    	}
    	
    	if (this.data.listtmp != null && this.data.listtmp != '') {
    		var node = this.liststore.getById(this.data.listtmp);
    		this.listpanel.getSelectionModel().select(node);
    	}
    	
    	if (this.data.detailtmp != null && this.data.detailtmp != '') {
    		var node = this.detailstore.getById(this.data.detailtmp);
    		this.detailpanel.getSelectionModel().select(node);
    	}

    },
    
    selectModel: function(fn, record, item) {
    	Ext.Ajax.request({
    		async: false,
    	    url: '/admin/website.do?choose',
    	    params: {
    	    	name: fn.name,
    	    	val: record.get('id')
    	    },
    	    success: function(response){
    	    	res = Ext.decode(response.responseText);
    	    	if (res.success) {
    	    		Ext.Msg.alert('提示', '已选择' + record.get('name'));
    	    	}
    	    },
    	    failure: function(response) {
    	    	Ext.Msg.alert('提示', '操作异常');
    	    }
    	});
    },
    
    scanWebsiteByHome: function() {
    	var win = Ext.create('Ext.window.Window', {
    	    title: '浏览模版效果',
    	    height: 600,
    	    width: 400,
    	    layout: 'fit',
    	    modal: true,
    	    items:[{
    	    	xtype: 'panel',
    	    	autoScroll: true,
    	    	autoLoad  : {
        			url : '/admin/view.jsp',
        			params : {
        				url : '/index.jhtml?wx=' + this.weixinId
        			}
        		}
    	    }],
    	    buttons: [{ 
	        	text: '关闭',
	        	scope: this,
	        	handler: function() {
	        		win.close();
	        	}
	        }]
    	    
    	}).show();
    },
    
    scanWebsiteByList: function() {
    	var win = Ext.create('Ext.window.Window', {
    	    title: '浏览模版效果',
    	    height: 600,
    	    width: 400,
    	    layout: 'fit',
    	    modal: true,
    	    items:[{
    	    	xtype: 'panel',
    	    	autoScroll: true,
    	    	autoLoad  : {
        			url : '/admin/view.jsp',
        			params : {
        				url : '/list.jhtml?wx=' + this.weixinId
        			}
        		}
    	    }],
    	    buttons: [{ 
	        	text: '关闭',
	        	scope: this,
	        	handler: function() {
	        		win.close();
	        	}
	        }]
    	    
    	}).show();
    },
    
    scanWebsiteByDetail: function() {
    	var win = Ext.create('Ext.window.Window', {
    	    title: '浏览模版效果',
    	    height: 600,
    	    width: 400,
    	    layout: 'fit',
    	    modal: true,
    	    items:[{
    	    	xtype: 'panel',
    	    	autoScroll: true,
    	    	autoLoad  : {
        			url : '/admin/view.jsp',
        			params : {
        				url : '/detail.jhtml?wx=' + this.weixinId
        			}
        		}
    	    }],
    	    buttons: [{ 
	        	text: '关闭',
	        	scope: this,
	        	handler: function() {
	        		win.close();
	        	}
	        }]
    	    
    	}).show();
    },
    
    onDestroy: function(){
        this.callParent(arguments);
    }
    
});