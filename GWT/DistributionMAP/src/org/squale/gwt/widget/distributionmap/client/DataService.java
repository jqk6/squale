package org.squale.gwt.widget.distributionmap.client;

import java.util.ArrayList;

import org.squale.gwt.widget.distributionmap.client.widget.data.Parent;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath( "data" )
public interface DataService
    extends RemoteService
{
    ArrayList<Parent> getData();
}
