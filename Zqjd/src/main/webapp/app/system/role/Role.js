Ext.define('SystemApp.View.Role', {
    extend: 'Ext.panel.Panel',
    xtype: 'role',
    
    initComponent: function() {
    	
    	Ext.apply(this, {
    		border: 0,
            layout: 'border',
            items: [this.createGrid()]
        });
    	this.createEditWindows();
    	this.createAuthorityWindows();
        this.callParent(arguments);
    },
    
    
    createGrid: function() {
    	var store = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields:['name', 'createDate', 'visible'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/role.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true
    	});
    	
    	this.grid = Ext.create('Ext.grid.Panel', {
    		border: 0,
    	    store: store,
    	    region: 'center',
    	    dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store: store,  
    	        dock: 'bottom',
    	        displayInfo: true
    	    }],
    	    columns: [{ 
    	    	text: '角色名称',  
    	    	dataIndex: 'name',
    	    	flex: 0.25
    	    }, {
    	    	text: '最后修改时间',
    	    	dataIndex: 'createDate',
    	    	flex: 0.25,
    	    	renderer: function(v){
    	    		if (v == null) return "";
    	    		return Ext.Date.format(new Date(v.time), 'Y-m-d H:i');
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
                    text : "权限",  
                    cls: 'icon-authority', 
                    scope: this,
                    handler: this.configAuthority
                }, {  
                    text : "编辑",  
                    cls: 'icon-edit', 
                    scope: this,
                    handler: this.editGrid
                }/*, {  
                    text : "删除",  
                    cls: 'icon-del',
                    scope: this,
                    handler: this.delGrid
                }*/]  
            }],
            tbar: ['-', { 
            	xtype: 'button', 
            	text: '刷新',
            	iconCls: 'icon-refresh',
            	scope: this,
            	handler: this.refreshGird
            }, '-', { 
            	xtype: 'button', 
            	text: '添加',
            	iconCls: 'icon-add',
            	scope: this,
            	handler: this.addGrid
            }, '->' ,{
            	xtype: 'textfield',
            	width: 180,
            	emptyText: '输入名称搜索',
            	id: 'Role.SearchName'
            }, '-', { 
            	xtype: 'button', 
            	text: '搜索',
            	iconCls: 'icon-search',
            	scope: this,
            	handler: this.searchGrid
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
			name: Ext.getCmp('Role.SearchName').getValue()
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
    	
    	this.editWin = Ext.create('Ext.window.Window', {
    	    title: '角色权限信息',
    	    height: 200,
    	    width: 500,
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
    	            anchor: '100%',
    	            labelStyle: 'color:green;padding-left:4px'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'name',
	    	            fieldLabel: '角色名称',
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　输入角色名称</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'combo',
	    	        	name: 'visible',
	    	        	fieldLabel: '是否显示',
	    	        	store: enabled,
	    	            queryMode: 'local',
	    	            valueField: 'value',
	    	            displayField: 'text',
	    	            editable: false,
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　设置是否显示</span>'
		        	}]
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
    
    createAuthorityWindows: function() {
    	this.treegrid = null;
    	
    	this.treeStore = Ext.create('Ext.data.TreeStore', {
    		fields:['name', 'parentId', 'id'],
            proxy: {
                type: 'ajax',
                url: '/admin/role.do?menu'
            },
            autoLoad: false
        });
    	
    	this.authorityWin = Ext.create('Ext.window.Window', {
    	    title: '权限设置',
    	    height: 550,
    	    width: 500,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	layout: 'fit',
    	    	fieldDefaults: {
    	            labelWidth: 75,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, this.treegrid = Ext.create('Ext.tree.Panel', {
    	        	xtype: 'treepanel',
    	            useArrows: true,
    	            rootVisible: false,
    	            store: this.treeStore,
    	            columns: [{ 
    	            	xtype: 'treecolumn',
    	    	    	text: '菜单名称',  
    	    	    	dataIndex: 'name',
    	    	    	flex: 1
    	    	    }],
    	            listeners: {  
    	                "checkchange": function(node, checked) {
    	                	if(checked) {
    	                		node.eachChild(function (child) {
    	                			chd(child,true);
    	                		});
    	                	} else {
    	                		node.eachChild(function (child) {
    	                			chd(child,false);
    	                		});
    	                	}
    	                	parentnode(node); //进行父级选中操作
    	                }
    	            }
    	        })]
    	    }],
	        buttons: [{ 
	        	text: '确定',
	        	scope: this,
	        	handler: this.submitConfig
	        }, { 
	        	text: '取消',
	        	scope: this,
	        	handler: function() {
	        		this.authorityWin.hide();
	        	}
	        }]
    	});
    },
    
    addGrid: function() {
		var form = this.editWin.down('form');
    	this.editWin.show();
    	form.getForm().reset();
    },
    
    configAuthority: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var form = this.authorityWin.down('form');
    	this.authorityWin.show();
    	
    	form.getForm().findField('id').setValue(record.get('id'));
    	this.treegrid.fireEvent('checkchange', this.treeStore.getRootNode(), false);
    	this.treeStore.proxy.extraParams = {
			roleId: record.get('id')
		};
		this.treeStore.load();
/*    	var treepanel = this.treegrid;
    	Ext.Ajax.request({
    	    url: '/admin/center.do?authority',
    	    params: {
    	    	id: record.get('id')
    	    },
    	    success: function(response){
    	    	var res = Ext.decode(response.responseText);
    	    	for (var i = 0; i < res.length; i++) {
    	    		var node = treepanel.getStore().getNodeById(res[i].authority);
    	    		if (node != undefined) {
    	    			node.set('checked', true);
    	    			treepanel.fireEvent('checkchange', node, true);
    	    		}
    	    	}
    	    },
    	    failure: function(response) {
    	    	Ext.Msg.alert('提示', '操作异常');
    	    }
    	});*/
    },
    
    submitConfig: function() {
    	var nodes = this.treegrid.getChecked();
    	var menus = [];
    	for (var i = 0; i < nodes.length; i++) {
    		if (!nodes[i].hasChildNodes()) {
    			menus.push(nodes[i].get('id'));
    		}
    	}
    	var grid  = this.grid;
    	var form = this.authorityWin.down('form');
    	var win = this.authorityWin;
    	
    	form.submit({
    		url: '/admin/role.do?config',
    		params: {
    			menus: menus
    		},
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
    
    editGrid: function(grid, rowIndex, colIndex) {
    	var form = this.editWin.down('form');
    	this.editWin.show();
    	form.getForm().reset();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    },
    
    submitFormDate: function() {
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	form.submit({
    		url: '/admin/role.do?save',
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
		Ext.MessageBox.confirm('确认', '是否删除该角色?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/role.do?delete',
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
    
		onDestroy: function(){
			this.editWin.destroy();
			this.authorityWin.destroy();
			this.callParent(arguments);
		}

});