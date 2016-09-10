Ext.define('SystemApp.View.Evaluat', {
    extend: 'Ext.panel.Panel',
    xtype: 'evaluat',
    
    initComponent: function() {
    	
    	Ext.apply(this, {
    		border: 0,
	        layout: 'border',
	        items: [this.createPanel()]
	    });
	    this.createWindows();
	    this.callParent(arguments);
    },
    
    createPanel: function() {
        var evluateStore = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields: ['id', 'ordernumber', 'username', 'phone', 'role', 'createDate', 'orders', 'user', 'flowStepRecPost', 'type', 'value'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/evaluat.do?list',
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true,
            groupField: 'ordernumber'
    	});
    	
    	this.grid = Ext.create('Ext.grid.Panel', {
    		border: 0,
			store: evluateStore,
	    	region: 'center',
	    	//columnLines: true,
	    	emptyText: '暂无数据可显示',
	    	features: [{
                ftype: 'groupingsummary',
                groupHeaderTpl: '{ordernumber} (共有{rows.length}条评价)',
                hideGroupedHeader: true,
                enableGroupingMenu: false,
                showSummaryRow: false
            }],
            dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store: evluateStore,  
    	        dock: 'bottom',
    	        displayInfo: true
    	    }],
    	    columns: [{
    	    	text: '订单号',
                flex: 1,
                dataIndex: 'ordernumber',
                hideable: false
    	    }, {
    	    	text: '评价人',
                flex: 0.08,
                dataIndex: 'username',
                hideable: false
    	    }, {
    	    	text: '联系电话',
                flex: 0.08,
                dataIndex: 'phone',
                hideable: false
    	    }, {
	    		text: '评价岗位',
                flex: 0.08,
                dataIndex: 'role',
                hideable: false
    	    }, {
	    		text: '评价',
                flex: 0.15,
                dataIndex: 'value',
                renderer: function(v, meta) {
                	var user = meta.record.data.user
                	var type = meta.record.data.type
                	var star = "";
                	if(type === 0) {
                		star += "星级：";
	                	for(var i=0; i<v; i++) {
	                		star += "<img src='/app/resources/default/images/star.png'/>";
	                	}
                	}
                	if(type === 1) {
                		meta.tdAttr = "data-qtip='<img src=\"" + v + "\" width=600/>'";
                		star = '<img src="'+ v + '" width=45 heigtht=45/>';
                	}
                	if(type === 2) {
                		var msg = "";
                		if(user === userInfo.id) msg += "我的回复：";
                		else msg = "客户留言：";
                		star += msg+v;
                	}
                	return star;
                }
    	    }, {
    	    	text: '评价时间',
    	    	dataIndex: 'createDate',
    	    	flex: 0.15,
    	    	renderer: function(v){
    	    		if (v == null) return "--";
    	    		return Ext.Date.format(new Date(v.time), 'Y-m-d H:i:s');
 	            }
    	    }, {
            	xtype: 'actiontextcolumn',
            	text: '主要操作',
            	flex: 0.25,
                items : [{  
                    text : "",  
                    cls: '', 
                    scope: this,
                    handler: this.replyGrid,
            		getClass: function(v, meta, rec, rowIndex, colIndex, store) {
            			var user = rec.get('user');
						var type = rec.get('type');
						if(type !== 2) {
							this.query('actiontextcolumn')[0].items[0].hide();
						}
						if(type === 2 && user !== userInfo.id) {
							this.query('actiontextcolumn')[0].items[0].text = "回复";
							return 'icon-news';
						}
						if(type === 2 && user === userInfo.id) {
							this.query('actiontextcolumn')[0].items[0].text = "编辑";
							return 'icon-edit';
						}
                    }
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
            }, '->', {
            	xtype: 'textfield',
            	width: 180,
            	emptyText: '输入姓名或电话号码搜索'
            }, '-', { 
            	xtype: 'button', 
            	text: '搜索',
            	iconCls: 'icon-search',
            	scope: this,
            	handler: this.searchGrid
            }, '-']
    	});
    	
    	return this.grid;
    },
    
    refreshGird: function() {
    	this.grid.getStore().reload();
    },
    
    searchGrid: function(me) {
    	var store = this.grid.getStore();
    	store.currentPage = 1;
    	store.proxy.extraParams = {
			phone: me.previousSibling('textfield').getValue()
		};
		store.load();
    },
    
    createWindows: function() {
    	this.replyWin = Ext.create('Ext.window.Window', {
    		title: '回复评论',
    	    height: 250,
    	    width: 300,
    	    border: 0,
    	    layout: 'border',
    	    resizable: false,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	fieldDefaults: {
    	            labelWidth: 65,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'hiddenfield',
    	        	name: 'orders'
    	        }, {
    	        	xtype: 'hiddenfield',
    	        	name: 'user'
    	        }, {
    	        	xtype: 'hiddenfield',
    	        	name: 'type'
    	        }, {
    	        	xtype: 'textarea',
    	            name: 'value',
    	            fieldLabel: '',
    	            height: 250,
    	            emptyText: '请输入你要回复的内容：',
    	            allowBlank: false
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormData
    	        }, { 
    	        	text: '关闭',
    	        	scope: this,
    	        	handler: function() {
    	        		this.replyWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
    replyGrid: function(grid, rowIndex, colIndex) {
		var form = this.replyWin.down('form');
    	var rec = grid.getStore().getAt(rowIndex);
    	this.replyWin.show();
    	form.loadRecord(rec);
    	form.getForm().findField('user').setValue(userInfo.id);
    	if(rec.get('user') !== userInfo.id) {
    		form.getForm().findField('id').setValue('');
    		form.getForm().findField('value').setValue('');
    		this.replyWin.setTitle("正在对 "+rec.get('username') + " 回复");
    	}else {
    		this.replyWin.setTitle("编辑我的回复");
    	}
    	
    },
    
    submitFormData: function() {
    	var grid  = this.grid;
    	var win = this.replyWin;
    	var form = win.down('form');
    	var tagerform = form.getForm();
        form.submit({
    		url: '/admin/evaluat.do?save',
    	    success: function(form, action) {
    	       //win.hide();
    	    	tagerform.findField('id').setValue('')
    	       tagerform.findField('value').setValue('')
    	       grid.getStore().reload();
    	       Ext.example.msg('提示', action.result.title);
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    delGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.grid;
		Ext.MessageBox.confirm('确认', '删除该客户评价无法恢复，请确认?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/evaluat.do?delete',
        	    params: {
        	        id: record.get('id')
        	    },
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.getStore().reload();
        	    	}
        	    	Ext.example.msg('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
    
    onDestroy: function() {
    	this.replyWin.destroy();
        this.callParent(arguments);
    }
});