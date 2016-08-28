Ext.define('SystemApp.View.Diymen', {
    extend: 'Ext.panel.Panel',
    xtype: 'diymen',
    
    initComponent: function() {
    	Ext.apply(this, {
            layout: 'border',
            items: [this.createGrid()]
        });
    	this.createEditWindows();
    	this.createConfigWindows();
        this.callParent(arguments);
    },
    
    
    createGrid: function() {
    	
    	var store = Ext.create('Ext.data.TreeStore', {
    		fields:['name', 'level', 'visible', 'url', 'markcode', 'parentId', 'id'],
            proxy: {
                type: 'ajax',
                url: '/admin/diymen.do?tree'
            },
            autoLoad: false
        });
    	
    	this.grid = Ext.create('Ext.tree.Panel', {
    		border: 0,
            region: 'center',
            useArrows: true,
            rootVisible: false,
            store: store,
            id: 'Diymen_TreeGrid',
            columns: [{ 
            	xtype: 'treecolumn',
    	    	text: '菜单名称',  
    	    	dataIndex: 'name',
    	    	flex: 0.25
    	    }, {
    	    	text: '等级排序',
    	    	dataIndex: 'level',
    	    	flex: 0.15
    	    }, {
    	    	text: '关键字',
    	    	dataIndex: 'markcode',
    	    	flex: 0.2,
    	    	renderer: function(v) {
                	if (v == "") return "--";
                	else return v;
                }
    	    }, {
    	    	text: '链接地址',
    	    	dataIndex: 'url',
    	    	flex: 0.25,
    	    	renderer: function(v) {
                	if (v == "") return "--";
                	else return v;
                }
    	    }, {
    	    	text: '是否显示',
    	    	dataIndex: 'visible',
    	    	flex: 0.15,
    	    	renderer: function(v) {
                	if (v) return "<font color=green>显示</font>";
                	else return "<font color=red>隐藏</font>";
                }
    	    }, {
            	xtype: 'actiontextcolumn',
            	text: '主要操作',
            	flex: 0.25,
                items : [{  
                    text : "编辑",  
                    cls: 'icon-edit', 
                    scope: this,
                    handler: this.editGrid
                }, {  
                    text : "删除",  
                    cls: 'icon-del',
                    scope: this,
                    handler: this.delGrid
                }]  
            }],
            tbar: [{ 
            	xtype: 'button', 
            	text: '刷新',
            	iconCls: 'icon-refresh',
            	scope: this,
            	handler: this.refreshGird
            }, { 
            	xtype: 'button', 
            	text: '添加',
            	iconCls: 'icon-add',
            	scope: this,
            	handler: this.addGrid
            }, '->' , { 
            	xtype: 'button', 
            	text: '授权设置',
            	iconCls: 'icon-config',
            	scope: this,
            	handler: this.configWx
            }, { 
            	xtype: 'button', 
            	text: '生成菜单',
            	iconCls: 'icon-send',
            	scope: this,
            	handler: this.createMenu
            }]
        });
    	
    	return this.grid;
    },
    
    refreshGird: function() {
    	this.grid.getStore().reload();
    },
    
    searchGrid: function() {
    	var store = this.grid.getStore();
    	store.currentPage = 1;
    	store.proxy.extraParams = {
			name: Ext.getCmp('Diymen.SearchName').getValue()
		};
		store.load();
    },
    
    createEditWindows: function() {
    	var enabled = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"显示", "value": true
    	    },{
    	    	"text":"隐藏", "value": false
    	    }]
    	});
    	
    	this.parentStore = Ext.create('Ext.data.Store', {
    	    fields: ['name', 'id', 'children'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/diymen.do?parent'
    	    },
            autoLoad: true
    	});
    	
    	
    	this.editWin = Ext.create('Ext.window.Window', {
    	    title: '编辑菜单信息',
    	    height: 350,
    	    width: 600,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	fieldDefaults: {
    	            labelWidth: 75,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'combo',
    	        	name: 'parentId',
    	        	fieldLabel: '上级菜单',
    	            store: this.parentStore,
    	            queryMode: 'remote',
    	            displayField: 'name',
    	            valueField: 'id',
    	            editable: false,
    	            allowBlank: false,
    	            listeners: {
    	            	scope: this,
    	            	expand: function(me, eOpts) {
    	            		me.getStore().reload();
    	            	},
    	            	change: function(me, newValue, oldValue, eOpts) {
    	            		var id = this.editWin.down("form").getForm().findField('id').getValue();
    	            		if(newValue===id) me.setValue("");
    	            	},
    	            	select: function(me, records, eOpts) {
    	            		if(records[0].data.children.length > 4){
    	            			Ext.example.msg("提示", "微信公众号二级菜单不能超过5个");
    	            			me.setValue("");
    	            		}
    	            	}
    	            }
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'name',
    	            fieldLabel: '菜单名称',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'numberfield',
    	        	name: 'level',
    	        	fieldLabel: '等级排序',
    	            minValue: 0,
    	            maxValue: 1000,
    	            allowBlank: false,
    	            value: 0
    	        }, {
                	xtype: 'radiogroup',
                	anchor: 'none',
                	fieldLabel: '触发方式',
                	layout: {autoFlex: false},
                    defaults: {name: 'cfType', margin: '0 20 0 0'},
                	items: [{
                        boxLabel: '关键字触发',
                        checked: true
                	}, {
                    	boxLabel: '自定义地址触发',
	                	listeners: {
	                		scope: this,
	                		change: function(me, newValue, oldValue, eOpts) {
	                			var markcode = this.editWin.down("form").getForm().findField("markcode");
	                			var url = this.editWin.down("form").getForm().findField("url");
	                			if(newValue) {
	                				markcode.setDisabled(true).setVisible(false);
	                				url.setDisabled(false).setVisible(true);
	                			}else {
	                				markcode.setDisabled(false).setVisible(true);
	                				url.setDisabled(true).setVisible(false);
	                			}
	                		}
	                	}
                	}]
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'url',
    	            disabled: true,
    	            hidden: true,
    	            fieldLabel: '链接地址',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'markcode',
    	            fieldLabel: '关键字',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'combo',
    	        	name: 'visible',
    	        	fieldLabel: '是否显示',
    	        	store: enabled,
    	            queryMode: 'local',
    	            valueField: 'value',
    	            displayField: 'text',
    	            editable: false,
    	            allowBlank: false
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormDate
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.editWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
    addGrid: function() {
    	var parentNodes = this.grid.getRootNode().childNodes;
    	if( parentNodes.length > 2 ) {
    		Ext.example.msg("提示", "微信目前限制一级菜单数量为3个");
    		this.parentStore.on('load', function() {
    			if(this.parentStore.getAt(0).get('id') == "") this.parentStore.removeAt(0);
		    	for(var i=0;i<parentNodes.length;i++){
		    		if(parentNodes[i].childNodes.length > 4) {
		    			this.parentStore.remove(parentNodes[i]);
		    		}
		    	};
    		}, this);
    	}
		var form = this.editWin.down('form');
    	this.editWin.show();
    	form.getForm().reset();
    	this.parentStore.reload();
    },
    
    editGrid: function(grid, rowIndex, colIndex) {
    	var form = this.editWin.down('form');
    	this.editWin.show();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    	if (rec.get('parentId') == 'root') {
    		form.getForm().findField('parentId').setValue("");
    	}
    	this.parentStore.reload();
    	var cfType = this.editWin.down("form").down("radiogroup");
    	var markcode = this.editWin.down("form").getForm().findField("markcode");
	    var url = this.editWin.down("form").getForm().findField("url");
    	if(rec.get('url')!==""){
    		cfType.items.get(1).setValue(true);
    		markcode.setDisabled(true).setVisible(false);
	        url.setDisabled(false).setVisible(true);
    	}else {
    		cfType.items.get(0).setValue(true);
    		markcode.setDisabled(false).setVisible(true);
	        url.setDisabled(true).setVisible(false);
    	}
    },
    
    submitFormDate: function() {
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	form.submit({
    		url: '/admin/diymen.do?save',
    	    success: function(form, action) {
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	       grid.getStore().reload();
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    delGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.grid;
		Ext.MessageBox.confirm('确认', '是否删除该菜单信息?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/diymen.do?delete',
        	    params: {
        	        id: record.get('id')
        	    },
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.getStore().reload();
        	    	}
        	    	Ext.Msg.alert('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
    
    createConfigWindows: function() {
    	
    	this.configWin = Ext.create('Ext.window.Window', {
    	    title: '设置授权信息',
    	    height: 200,
    	    width: 480,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	fieldDefaults: {
    	            labelWidth: 75,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'appId',
    	            fieldLabel: 'AppId',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'appSecret',
    	            fieldLabel: 'AppSecret',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'component',
	        		width: '100%',
	        		margin: '0 0 5 80',
					html: '<a href="https://mp.weixin.qq.com" target="view_window">参数如何获取?点击登陆微信公众号官方平台->开发->基本配置</a>'
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormConfig
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.configWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
    configWx: function() {
    	var form = this.configWin.down('form');
    	this.configWin.show();
    	form.getForm().reset();
    	Ext.Ajax.request({
    	    url: '/admin/diymen.do?wxinfo',
    	    success: function(response){
    	    	var res = Ext.decode(response.responseText);
	    		form.getForm().findField('appId').setValue(res.appId);
	    		form.getForm().findField('appSecret').setValue(res.appSecret);
    	    },
    	    failure: function(response) {
    	    	var res = Ext.decode(response.responseText);
    	    	Ext.Msg.alert('提示', res.title);
    	    }
    	});
    },
    
    submitFormConfig: function() {
    	var form = this.configWin.down('form');
    	var win = this.configWin;
    	form.submit({
    		url: '/admin/diymen.do?wxconfig',
    	    success: function(form, action) {
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    createMenu: function() {
		Ext.MessageBox.confirm('确认', '是否生成自定义菜单?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/diymen.do?createMenu',
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
    
    onDestroy: function(){
    	this.editWin.destroy();
    	this.configWin.destroy();
        this.callParent(arguments);
    }

});