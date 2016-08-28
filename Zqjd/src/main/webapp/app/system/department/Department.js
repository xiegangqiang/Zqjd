Ext.define('SystemApp.View.Department',{
	extend: 'Ext.panel.Panel',
    xtype: 'department',
    
    initComponent: function() {
    	Ext.apply(this, {
    		border: 0,
            layout: 'border',
            items: [this.createGrid()]
        });
    	this.createEditWindows();
        this.callParent(arguments);
    },
	
    createGrid: function() {
    	
    	var store = Ext.create('Ext.data.TreeStore', {
    		fields:['id', 'name','address', 'phone', 'fax', 'shortnum', 'remark', 'level', 'visible', 'parentId', 'modifyDate', 'admins'],
            proxy: {
                type: 'ajax',
                url: '/admin/department.do?tree'
            },
            autoLoad: false
        });
    	
    	this.grid = Ext.create('Ext.tree.Panel', {
    		border: 0,
            region: 'center',
            useArrows: true,
            rootVisible: false,
            store: store,
            columns: [{ 
            	xtype: 'treecolumn',
    	    	text: '部们名称',  
    	    	dataIndex: 'name',
    	    	flex: 0.10
    	    }, { 
    	    	text: '部门人数',  
    	    	dataIndex: 'admins',
    	    	flex: 0.05,
    	    	renderer: function(v) {
                	return v.length
                }
    	    }, { 
    	    	text: '部门简码',  
    	    	dataIndex: 'shortnum',
    	    	flex: 0.15
    	    }, {
    	    	text: '部门地址',  
    	    	dataIndex: 'address',
    	    	flex: 0.25
    	    }, {
    	    	text: '等级排序',
    	    	dataIndex: 'level',
    	    	flex: 0.1
    	    }, {
    	    	text: '是否显示',
    	    	dataIndex: 'visible',
    	    	flex: 0.1,
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
            tbar: ['-', { 
            	xtype: 'button', 
            	text: '刷新',
            	iconCls: 'icon-refresh',
            	scope: this,
            	handler: this.refreshGird
            }, '-', { 
            	xtype: 'button', 
            	text: '添加部门',
            	iconCls: 'icon-add',
            	scope: this,
            	handler: this.addGrid
            }, '->' ,{
            	xtype: 'textfield',
            	width: 180,
            	emptyText: '输入名称搜索',
            	id: 'Department.SearchName'
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
			name: Ext.getCmp('Department.SearchName').getValue()
		};
		store.load();
    },
    
    createEditWindows: function(){
    	
    	var enabled = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"显示", "value": true
    	    },{
    	    	"text":"隐藏", "value": false
    	    }]
    	});
    
    	this.parentStore = Ext.create('Ext.data.Store', {
    	    fields: ['name', 'id'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/department.do?parent'
    	    },
            autoLoad: true
    	});
    
    	this.editWin = Ext.create('Ext.window.Window',{
    		title: '编辑部门信息',
    	    height: 500,
    	    width: 700,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    		items: [{
    			xtype: 'form',
    			region: 'center',
    			bodyPadding: 10,
    			fieldDefaults: {
    				labelWidth: 70,
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
	    	        	xtype: 'combo',
	    	        	name: 'parentId',
	    	        	fieldLabel: '直属部门',
	    	            store: this.parentStore,
	    	            queryMode: 'remote',
	    	            displayField: 'name',
	    	            valueField: 'id',
	    	            editable: false,
	    	            allowBlank: false,
	    	            listeners: {
	    	            	expand: function(me, eOpts) {
	    	            		me.getStore().reload();
	    	            	}
	    	            }
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请选择部门</span>'
		        	}]
    			}, {
    				xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'name',
	    	            fieldLabel: '部门名称',
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入部门名称</span>'
		        	}]
    			}, {
    				xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'shortnum',
	    	            fieldLabel: '部门简码',
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入部门简码</span>'
		        	}]
    			}, {
    				xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'phone',
	    	            fieldLabel: '部门电话'
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入部门电话号码</span>'
		        	}]
    			}, {
    				xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'fax',
	    	            fieldLabel: '部门传真'
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入部门传真</span>'
		        	}]
    			}, {
    				xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'numberfield',
	    	        	name: 'level',
	    	        	fieldLabel: '等级排序',
	    	            minValue: 0,
	    	            value: 0,
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　等级排序影响显示的排列顺序，数值越小越靠前</span>'
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
	    	            value:true,
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　设置是否显示</span>'
		        	}]
    			}, {
    				xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	        	name: 'address',
	    	        	fieldLabel: '部门地址',
	    	        	width: 600
	    	        }]
    			}, {
    				xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textarea',
	    	        	name: 'remark',
	    	        	fieldLabel: '备注说明',
	    	        	width: 600
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
    
   addGrid: function() {
		var form = this.editWin.down('form');
    	this.editWin.show();
    	form.getForm().reset();
    	form.getForm().findField('shortnum').setDisabled(false);
    	this.parentStore.reload();
    },
    
   editGrid: function(grid, rowIndex, colIndex) {
    	var form = this.editWin.down('form');
    	this.editWin.show();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    	form.getForm().findField('shortnum').setDisabled(true);
    	if (rec.get('parentId') == 'root') {
    		form.getForm().findField('parentId').setValue("");
    	}
    },
    
   submitFormDate: function() {
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	form.submit({
    		url: '/admin/department.do?save',
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
		Ext.MessageBox.confirm('确认', '是否删除该部门信息?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/department.do?delete',
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
        this.callParent(arguments);
    }
    
});






























