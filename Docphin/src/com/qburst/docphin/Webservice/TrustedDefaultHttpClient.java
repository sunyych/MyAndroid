package com.qburst.docphin.Webservice;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

import com.qburst.docphin.R;

import android.content.Context;


public class TrustedDefaultHttpClient extends DefaultHttpClient
{

    final Context context;

    public TrustedDefaultHttpClient( ClientConnectionManager connManager, HttpParams params,
        Context context )
    {
        super( connManager, params );
        this.context = context;
    }

    public TrustedDefaultHttpClient( Context context )
    {
        this.context = context;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager()
    {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(
            new Scheme( "http", PlainSocketFactory.getSocketFactory(), 80 ) );
        registry.register( new Scheme( "https", newSslSocketFactory(), 443 ) );
        return new SingleClientConnManager( getParams(), registry );
    }

    private SSLSocketFactory newSslSocketFactory()
    {
        try
        {
            KeyStore trusted = KeyStore.getInstance( "BKS" );
            InputStream in = context.getResources().openRawResource( R.raw.mystore );
            try
            {
                trusted.load( in, "mysecret".toCharArray() );
            }
            finally
            {
                in.close();
            }
            return new SSLSocketFactory( trusted );
        }
        catch ( Exception e )
        {
            throw new AssertionError( e );
        }
    }
}
