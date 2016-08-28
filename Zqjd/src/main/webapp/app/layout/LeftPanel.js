Ext.define('SystemApp.View.Layout.LeftPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.layout.leftpanel',
    animCollapse: true,
    layout: 'fit',
    title: '菜单导航',

    initComponent: function(){
        Ext.apply(this, {
			layout: {
				type: 'accordion',
				//titleCollapse: false,
				fill: false,
				animate: true
			},
            items: []
        });
        
        
        this.addEvents(
            'menuselect'
        );
        this.callParent(arguments);
        
        this.createView();
    },

    createView: function(){
    	this.initAndLoadView('/admin/menu.do?tree');
    },

    onSelectionChange: function(obj, record){
        if (record) {
            this.loadFeed(record);
        }
    },

    onLoadClick: function(){
        this.loadFeed(this.menu.activeFeed);
    },

    loadFeed: function(rec){
        if (rec) {
            this.fireEvent('menuselect', this, rec.get('name'), rec.get('namespace'));
        }
    },
    
    initAndLoadView: function(url) {
    	//默认系统自带菜单
    	if( url.indexOf('module') < 0 ) url += '&module=40288103510e220701510e237c3c0000';
		var menuDatas = [];
    	Ext.Ajax.request({
    	    url: url,
    	    async :  false,
    	    success: function(response) {
    	    	var res = Ext.decode(response.responseText);
    	    	menuDatas = res;
    	    }
    	});
    	
    	var view = [];
    	for (var i = 0; i < menuDatas.length; i++) {
    		/***************************这里是树形三级权限控制***********************************/
/*    		var panel = Ext.create('Ext.panel.Panel',{
	    			title: '',
	    			//autoScroll: true,
	    			layout: 'fit',
	    			items: [{
	    				xtype: 'treepanel',
	    				deferRowRender: true,
	    				rootVisible: false,
	    				root: {},
						listeners: {
		                    scope: this,
		                    itemclick: function(view, record, item, index, e, eOpts) {
		                    	if(record.get('id')){
		                    		this.fireEvent('menuselect', this, record.get('text'), record.get('id'));
		                    	}
		                    }
		                }
	    			}]
	    	});
	    	panel.setTitle(menuDatas[i].name);
	    	var root = panel.down('treepanel').getRootNode();
	    	for(var j = 0; j < menuDatas[i].children.length; j++){
		    	if(menuDatas[i].children[j].children.length == 0){
		    		root.appendChild({id: menuDatas[i].children[j].namespace, text: menuDatas[i].children[j].name, leaf: true, icon: menuDatas[i].children[j].img});
		    	}
		    	if(menuDatas[i].children[j].children.length > 0){
		    		var parent = root.appendChild({text: menuDatas[i].children[j].name});
		    		var child = parent.insertChild(j, {id: menuDatas[i].children[j].children[j].namespace, text: menuDatas[i].children[j].children[j].name, leaf: true, icon: menuDatas[i].children[j].children[j].img});
		    		for(var k = 1; k < menuDatas[i].children[j].children.length; k++){
			    		parent.insertBefore({id: menuDatas[i].children[j].children[k].namespace, text: menuDatas[i].children[j].children[k].name, leaf: true, icon: menuDatas[i].children[j].children[k].img}, child); 
		    		}
		    	}
    		}
	    	view.push(panel);*/
	    	/*******************下面是二级权限控制***********************/
    		view.push({
    			xtype: 'panel',
    			title: menuDatas[i].name,
    			autoScroll: true,
    			items: [{
    				xtype: 'dataview',
    				autoScroll: true,
    				height: menuDatas[i].children.length*40,
    				store: Ext.create('Ext.data.Store', {
    	                model: 'Menu',
    	                data: menuDatas[i].children
    	            }),
    	            cls: 'feed-list',
    	            itemSelector: '.feed-list-item',
    	            overItemCls: 'feed-list-item-hover',
    	            tpl: '<tpl for="."><div class="feed-list-item"><img src="{img}"/><span>{name}</span></div></tpl>',
//    	            listeners: {
//    	                mode: 'SINGLE',
    	                listeners: {
    	                    scope: this,
    	                    itemclick: this.onSelectionChange
    	                }
//    	            }
    			}]
    		});
    	}
    	this.removeAll();
    	this.add(view);
    }

});
