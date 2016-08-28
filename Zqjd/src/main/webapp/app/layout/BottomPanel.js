Ext.define('SystemApp.View.Layout.BottomPanel', {
    extend: 'Ext.Container',
    alias: 'widget.layout.bottompanel',
    id: 'app-footer',
    region: 'south',
    layout: {
        type: 'hbox',
        align: 'middle'
    },
    initComponent: function() {
    	
        this.items = [{
            xtype: 'toolbar',
            flex: 1,
            items:[{
            	xtype: 'component',
            	html: '版权所有 Copyright © 2015 xysoft. All Rights Reserved V1.0 ',
            	style: 'color: #333333;'
            }, '-', '->', {
            	id: 'app-bottom-news',
            	xtype:'button',
            	iconCls: 'icon-more',
                menu: [{
                	text: '历史消息',
                	iconCls: 'icon-history',
                	handler: function() {
                		var historyWin = this.up('toolbar').up().historyWin;
                		historyWin.down('grid').getStore().reload();
                		historyWin.show();
                	}
                }, {
                	text: '最新消息',
                	iconCls: 'icon-news',
                	handler: function() {
                		this.up('toolbar').up().newsWin.showAt(document.body.clientWidth-350, document.body.clientHeight-290);
                	}
                }]
            }, {
            	xtype: 'component',
	        	html: '<a class="systemtime" >当前系统时间：</a>',
	        	width: 150
            }, {
            	id: 'app-clock',
	        	xtype: 'button',
	        	listeners: {
			      render: function(button, eOpts) {
			       		Ext.TaskManager.start({
					    run: function() {
					    	button.setText(Ext.Date.format(new Date(), 'g:i:s A'));
					    },
					    interval: 1000
					  });
			      }
			    }
            }]
        }, this.createWindows()];
        this.callParent();
    },//初始化结束
    
    createWindows: function(){
    	var store = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'letter', 'admin', 'modifyDate', 'state', 'mesasge'],
    	    data: []
    	});
    	Ext.Ajax.request({
		    url: '/admin/letter.do?info&admin=' + userInfo.id + '&state=false',
		    async :  false,
		    success: function(response){
		    	var res = Ext.decode(response.responseText);
		    	for (var i = 0; i < res.length; i++) {
    	    		store.add(res[i]);
    	    	}
		    }
	    });
	    
    	this.newsWin = Ext.create('Ext.window.Window', {
    		title: '最新通知',
			width: 350,
			height: 250,
			shadow: true,
			animateTarget: 'app-bottom-news',
	        autoScroll: true,
	        resizable: false,
	        draggable: false,
	        closeAction: 'hide',
	        bodyStyle: 'padding: 5px;',
	        items: [{
				xtype: 'component',
				style: 'padding: 10px;',
				html: store.count()>0?store.getAt(0).data.mesasge.content : '暂无最新消息'
			}],
	        listeners: {
	        	show: function(win, eOpts) {
	        		win.showAt(document.body.clientWidth-350, document.body.clientHeight-290);
	        	}
	        },
	        buttons: [{
	        	xtype: 'button',
	        	text: '我知道了',
	        	handler: function(button, eOpts) {
	        		if(store.count()>0) {
		            	Ext.Ajax.request({
		            	    url: '/admin/letter.do?read&type=0&admin=' + userInfo.id + '&letter=' + store.getAt(0).data.mesasge.id,
		            	    async :  false
		            	});
	        		}
	            	button.up('window').hide();
	        	}
	        }, {
	        	xtype: 'button',
	        	text: '彻底删除',
	        	handler: function(button, eOpts) {
	        		if(store.count()>0) {
		            	Ext.Ajax.request({
		            		url: '/admin/letter.do?read&type=1&admin=' + userInfo.id + '&letter=' + store.getAt(0).data.mesasge.id,
		            	    async :  false
		            	});
	        		}
	            	button.up('window').hide();
	        	}
	        }]
    	});
    	if(store.count() >0) this.newsWin.show();
    	
    	var historystore = Ext.create('Ext.data.Store', {
    	    fields:['id', 'letter', 'admin', 'modifyDate', 'state', 'mesasge'],
    	    proxy: {
                type: 'ajax',
                url: '/admin/letter.do?info&admin=' + userInfo.id + '&state=true'
    	    },
            autoLoad: true
    	});
    	this.historyWin = Ext.create('Ext.window.Window', {
    		title: '查看历史消息',
			width: 400,
			height: 300,
			shadow: true,
			animateTarget: 'app-bottom-news',
	        autoScroll: true,
	        resizable: false,
	        draggable: false,
	        closeAction: 'hide',
	        items: [{
	        	xtype: 'grid',
	        	store: historystore,
	        	columns: [{
	        		text: "发布时间", 
	        		dataIndex: 'modifyDate',
	        		flex: 0.3,
	        		renderer: function(v){
    	    		if (v == null) return "--";
    	    			return Ext.Date.format(new Date(v.time), 'Y-m-d H:i');
 	            	}
	        	}],
			    plugins: [{
			        ptype: 'rowexpander',
			        rowBodyTpl : new Ext.XTemplate(
			            '<p><b>消息内容:</b> {mesasge.content}</p>')
			    }]
	        }]
    	});
    }
    
});//js结束
