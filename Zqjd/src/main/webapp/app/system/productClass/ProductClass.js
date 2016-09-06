Ext.define('SystemApp.View.ProductClass',{
	extend: 'Ext.panel.Panel',
    xtype: 'productClass',
    
    initComponent: function() {
    	Ext.apply(this, {
    		border: 0,
            layout: 'border',
            items: [this.createGrid()]
        });
    	this.createEditWindows();
    	this.createImageUpload();
        this.callParent(arguments);
    },
	
    createGrid: function() {
    	
    	var store = Ext.create('Ext.data.TreeStore', {
    		fields:['id', 'name', 'visible', 'level', 'img', 'markcode', 'descript'],
            proxy: {
                type: 'ajax',
                url: '/admin/productClass.do?tree'
            },
            autoLoad: false
        });
    	
    	this.grid = Ext.create('Ext.tree.Panel', {
    		border: 0,
            region: 'center',
            useArrows: true,
            rootVisible: false,
            columnLines: true,
            store: store,
            columns: [{ 
            	xtype: 'treecolumn',
    	    	text: '分类名称',  
    	    	dataIndex: 'name',
    	    	flex: 0.1
    	    }, {
    	    	text: '分类小图',
    	    	dataIndex: 'img',
    	    	flex: 0.06,
    	    	renderer: function(v, meta){
    	    		meta.tdAttr = "data-qtip='<img src=\"" + v + "\" width=128 heigtht=128/>'";
    	    		return '<img src="'+ v + '" width=45 heigtht=45 />';
    	    	}
    	    }, {
    	    	text: '关键字',  
    	    	dataIndex: 'markcode',
    	    	flex: 0.1
    	    }, { 
    	    	text: '分类描述',  
    	    	dataIndex: 'descript',
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
            	text: '添加分类',
            	iconCls: 'icon-add',
            	scope: this,
            	handler: this.addGrid
            }, '->' ,{
            	xtype: 'textfield',
            	width: 180,
            	emptyText: '输入名称搜索',
            	id: 'ProductClass.SearchName'
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
			name: Ext.getCmp('ProductClass.SearchName').getValue()
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
    	        url: '/admin/productClass.do?parent'
    	    },
            autoLoad: true
    	});
    
    	this.editWin = Ext.create('Ext.window.Window',{
    		title: '编辑产品分类信息',
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
	    	        	fieldLabel: '上级分类',
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
						html: '<span style="color:#aaa">　请选择上一级分类</span>'
		        	}]
    			}, {
    				xtype:"container",
		        	layout: 'column',
		        	margin: 10,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'name',
	    	            fieldLabel: '分类名称',
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　请输入部门名称</span>'
		        	}]
    			}, {
    	        	xtype: 'container',
    	        	layout: 'column',
    	        	margin: 10,
    	        	items: [{
    	        		xtype: 'textfield',
        	            name: 'img',
        	            fieldLabel: '分类小图',
        	            columnWidth: 0.8,
        	            listeners: {
        	            	change: function(me) {
        	            		me.up('container').items.get(2).setTooltip('<img src="' + me.getValue() + '"  width="100%"/>');
        	            	}
        	            }
    	        	}, {
    	        		xtype: 'button',
    	        		text: '上传图片',
    	        		columnWidth: 0.1,
    	        		style: 'margin-left:3px',
    	        		scope: this,
    	        		handler: function(me) {
    	        			var form = this.uploadImageWin.down('form');
					    	this.uploadImageWin.show();
					    	form.getForm().reset();
					    	form.getForm().findField('id').setValue(fn.previousNode().getName());
    	        		}
    	        	}, {
    	        		xtype: 'button',
    	        		text: '预览图片',
    	        		columnWidth: 0.1,
    	        		scope: this,
    	        		style: 'margin-left:3px'
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
	    	        	xtype: 'textarea',
	    	        	name: 'descript',
	    	        	fieldLabel: '描述说明',
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
    
    createImageUpload: function() {
    	this.uploadImageWin = Ext.create('Ext.window.Window', {
    	    title: '上传图片',
    	    height: 150,
    	    width: 400,
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
    	            xtype: 'filefield',
    	            name: 'upload',
    	            fieldLabel: '图片文件',
    	            msgTarget: 'side',
    	            buttonText: '选择图片...'
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.uploadImageForm
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.uploadImageWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
    uploadImageForm: function() {
    	var targetForm = this.editWin.down('form').getForm();
    	var form = this.uploadImageWin.down('form');
    	var targetName = form.getForm().findField('id').getValue();  
    	var win = this.uploadImageWin;
    	form.submit({
    		url: '/admin/upload.do?save',
    	    success: function(form, action) {
    	       targetForm.findField(targetName).setValue(action.result.msg);
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
   addGrid: function() {
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
    },
    
   submitFormDate: function() {
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	form.submit({
    		url: '/admin/productClass.do?save',
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
		Ext.MessageBox.confirm('确认', '删除该分类会删除下面产品信息?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/productClass.do?delete',
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






























