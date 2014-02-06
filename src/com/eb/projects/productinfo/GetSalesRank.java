
package com.eb.projects.productinfo;

/******************************************************************************* 
 *  Copyright 2008-2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  
 *  You may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 *  This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 *  CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 *  specific language governing permissions and limitations under the License.
 * ***************************************************************************** 
 * 
 *  Marketplace Web Service Products Java Library
 *  API Version: 2011-10-01
 * 
 */

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonservices.mws.products.MarketplaceWebServiceProducts;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.ASINIdentifier;
import com.amazonservices.mws.products.model.AttributeSetList;
import com.amazonservices.mws.products.model.CompetitivePriceList;
import com.amazonservices.mws.products.model.CompetitivePriceType;
import com.amazonservices.mws.products.model.CompetitivePricingType;
import com.amazonservices.mws.products.model.GetMatchingProductForIdRequest;
import com.amazonservices.mws.products.model.GetMatchingProductForIdResponse;
import com.amazonservices.mws.products.model.GetMatchingProductForIdResult;
import com.amazonservices.mws.products.model.IdListType;
import com.amazonservices.mws.products.model.IdentifierType;
import com.amazonservices.mws.products.model.LowestOfferListingList;
import com.amazonservices.mws.products.model.LowestOfferListingType;
import com.amazonservices.mws.products.model.MoneyType;
import com.amazonservices.mws.products.model.NumberOfOfferListingsList;
import com.amazonservices.mws.products.model.OfferListingCountType;
import com.amazonservices.mws.products.model.OfferType;
import com.amazonservices.mws.products.model.OffersList;
import com.amazonservices.mws.products.model.PriceType;
import com.amazonservices.mws.products.model.Product;
import com.amazonservices.mws.products.model.ProductList;
import com.amazonservices.mws.products.model.QualifiersType;
import com.amazonservices.mws.products.model.RelationshipList;
import com.amazonservices.mws.products.model.ResponseMetadata;
import com.amazonservices.mws.products.model.SalesRankList;
import com.amazonservices.mws.products.model.SalesRankType;
import com.amazonservices.mws.products.model.SellerSKUIdentifier;
import com.amazonservices.mws.products.model.ShippingTimeType;
import com.eb.configure.ProductsConfig_MARKET_FROM;
import com.eb.configure.config_marketplace;
import com.eb.data.database.business.database_indexposition_business;
import com.eb.data.database.business.database_inventory_business;
import com.eb.data.dataobject.Item_InventoryExtension2;

/**
 *
 * Get Matching Product For Id  Samples
 *
 *
 */

public class GetSalesRank {

    /**
     * Just add few required parameters, and try the service
     * Get Matching Product For Id functionality
     *
     * @param args unused
     */
	
	private static database_inventory_business my_db;
	private static database_indexposition_business my_db_position;
	// private static int position;
	private final static String dbpositionname = config_marketplace.indexposition;
	private static final int DATABASE_STEP = config_marketplace.DATABASE_STEP;
	private static List<Item_InventoryExtension2> gaijialist;
	// private static BufferedWriter outFile;
	
	public static void  gaijiafunc(int startpoint, int endpoint ) 
			throws IOException, SQLException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		// get gaijia products
		
		gaijialist = my_db.getListFromIdArrange2( startpoint, endpoint );

		// delete the duplicated asin.
		
		Map<String, Item_InventoryExtension2> gaijialist_temp = new HashMap<String, Item_InventoryExtension2>();
		
		for ( int k = 0; k < gaijialist.size(); k++ )
		{
			if ( !gaijialist_temp.containsKey(gaijialist.get(k).isbn) )
			{
				gaijialist_temp.put(gaijialist.get(k).isbn, gaijialist.get(k));
			}
			else
			{
				gaijialist.remove(k);
				k--;
			}
		}
		
		// if no order, return.
		if (gaijialist.size() == 0) 
		{	
			return; 
		}
		
		// get chengbenjia 
		getChengbenjia();    
	}
	
	private static void getChengbenjia() throws IOException, SQLException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
        /*       
	    String wordsoutput = "producinfo_us2.txt";
        File outf = new File(wordsoutput );
        if (outf.exists()) {
         //   //System.out.println("file exit");
        } 
        else {
         //   //System.out.println("file does not exit, creating");
            if (outf.createNewFile()) {
         //    //System.out.println("succeed in creating");
            } 
            else {
             //System.out.println("fail to create file");
            }
       }
       */     
              
    	for ( int i = 0; i < gaijialist.size(); i++ )
    	{
    		if (gaijialist.get(i) == null)
    		{
    			continue;
    		}	
    	}
    	
    	for (int i = 0; i <= (gaijialist.size()/ 20 ) ; i++ )
        {
    	//	outFile = new BufferedWriter(new FileWriter(outf,true));
    		List<Item_InventoryExtension2> sub_ListSku = new ArrayList<Item_InventoryExtension2>();
         	// List<AsinProduct> aplist = new ArrayList<AsinProduct>();
         	
         	if ((i + 1) * 20  <= gaijialist.size() ){
         	    sub_ListSku = gaijialist.subList(i * 20, ( i + 1 ) * 20 );
         	}
         	else
         	{
         		sub_ListSku = gaijialist.subList(i * 20, gaijialist.size() );
         	}
         		
         	if (sub_ListSku.size() != 0)
         	{
         	//	GetSalesRank.getInfo(sub_ListSku);	  
         		for (int k = 0; k < sub_ListSku.size(); k++ )
         		{
         			GetSalesRank.getInfo(sub_ListSku.subList(k, k+1));	
         			
         		}
         	}
        // 	outFile.close();
       //	Thread.sleep(1000);
        }

	}
	
	public static void getInfo(List<Item_InventoryExtension2> sub_ListSku ) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		  MarketplaceWebServiceProducts service = new MarketplaceWebServiceProductsClient(
				  ProductsConfig_MARKET_FROM.accessKeyId, 
				  ProductsConfig_MARKET_FROM.secretAccessKey, 
				  ProductsConfig_MARKET_FROM.applicationName, 
				  ProductsConfig_MARKET_FROM.applicationVersion, 
				  ProductsConfig_MARKET_FROM.config);

      /************************************************************************
       * Uncomment to try out Mock Service that simulates Marketplace Web Service Products 
       * responses without calling Marketplace Web Service Products  service.
       *
       * Responses are loaded from local XML files. You can tweak XML files to
       * experiment with various outputs during development
       *
       * XML files available under com/amazonservices/mws/products/mock tree
       *
       ***********************************************************************/
      // MarketplaceWebServiceProducts service = new MarketplaceWebServiceProductsMock();

      /************************************************************************
       * Setup request parameters and uncomment invoke to try out 
       * sample for Get Matching Product For Id 
       ***********************************************************************/
       GetMatchingProductForIdRequest request = new GetMatchingProductForIdRequest();
       request.setSellerId(ProductsConfig_MARKET_FROM.sellerId);
       
       request.setMarketplaceId(ProductsConfig_MARKET_FROM.marketplaceId);
       request.setIdType("ASIN");   // SellerSKU
       
       String skuString = "";
       
       for ( int i = 0; i < sub_ListSku.size(); i++ )
       {
    	   if (skuString.equals(""))
    	   {
    		   skuString += sub_ListSku.get(i).isbn;
    	   }
    	   else
    	   {
    		   skuString += "," + sub_ListSku.get(i).isbn;
    	   }
       }
              
     //  String skuString =   "B003K19RWI"; //  "NEW-MASS-06412";  //
       
       String skuArray[]=skuString.split(",");  
       
       List<String> ListSku = new ArrayList<String>();
       
       for (int i = 0; i < skuArray.length; i++ )
       {
    	   ListSku.add(skuArray[i]);
       }
       
       IdListType ity = new IdListType();
       ity.setId(ListSku);
              
       request.setIdList(ity);
       
       invokeGetMatchingProductForId(service, request);
      
	}
	
    public static void main(String... args) throws IOException, SQLException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException 
    {
         
    	my_db = new database_inventory_business();

    	my_db_position = new database_indexposition_business();
    	
         // @TODO: set request parameters here
 		int  totalproducts = my_db.getMaxId();
		
		// each time deal with 10,000 products

		int nowindex = my_db_position.getValueFromName(dbpositionname);
		long time = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(time);
		my_db_position.insertStartPoint(dbpositionname,nowindex,timestamp);
		if ( nowindex == totalproducts )
		{
			nowindex = 1;
		}
		
		//position = nowindex;
		
		while (true)
		{
			while ( nowindex < totalproducts )
			{
				
				if (nowindex + DATABASE_STEP < totalproducts )
				{
					gaijiafunc(nowindex, nowindex + DATABASE_STEP -1);
					nowindex += DATABASE_STEP;
					my_db_position.update(dbpositionname, nowindex);
				}
				else
				{
					gaijiafunc(nowindex, totalproducts);
					nowindex = totalproducts;
					my_db_position.update(dbpositionname, nowindex);
				}
	
				totalproducts = my_db.getMaxId();
			}
			nowindex = 1;
		}

    }
                                   
    /**
     * Get Matching Product For Id  request sample
     * GetMatchingProduct will return the details (attributes) for the
     * given Identifier list. Identifer type can be one of [SKU|ASIN|UPC|EAN|ISBN|GTIN|JAN]
     *   
     * @param service instance of MarketplaceWebServiceProducts service
     * @param request Action to invoke
     * @throws IOException 
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    
    public static void invokeGetMatchingProductForId(MarketplaceWebServiceProducts service, GetMatchingProductForIdRequest request) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        try {

        	String asin = "";
        	
            GetMatchingProductForIdResponse response = service.getMatchingProductForId(request);
            //System.out.println ("GetMatchingProductForId Action Response");
            //System.out.println ("=============================================================================");
            //System.out.println ();

            //System.out.println("    GetMatchingProductForIdResponse");
            //System.out.println();
            
            java.util.List<GetMatchingProductForIdResult> getMatchingProductForIdResultList = response.getGetMatchingProductForIdResult();
            for (GetMatchingProductForIdResult getMatchingProductForIdResult : getMatchingProductForIdResultList) {
                //System.out.println("        GetMatchingProductForIdResult");
                //System.out.println();
            if (getMatchingProductForIdResult.isSetId()) {
                //System.out.println("        Id");
                //System.out.println();
                //System.out.println("            " + getMatchingProductForIdResult.getId());
                //System.out.println();
            } 
            if (getMatchingProductForIdResult.isSetIdType()) {
                //System.out.println("        IdType");
                //System.out.println();
                //System.out.println("            " + getMatchingProductForIdResult.getIdType());
                //System.out.println();
            } 
            if (getMatchingProductForIdResult.isSetStatus()) {
                //System.out.println("        status");
                //System.out.println();
                //System.out.println("            " + getMatchingProductForIdResult.getStatus());
                //System.out.println();
            } 
                if (getMatchingProductForIdResult.isSetProducts()) {
                    //System.out.println("            Products");
                    //System.out.println();
                    ProductList  products = getMatchingProductForIdResult.getProducts();
                    java.util.List<Product> productList = products.getProduct();
                    for (Product product : productList) {
                        //System.out.println("                Product");
                        //System.out.println();
                        if (product.isSetIdentifiers()) {
                            //System.out.println("                    Identifiers");
                            //System.out.println();
                            IdentifierType  identifiers = product.getIdentifiers();
                            if (identifiers.isSetMarketplaceASIN()) {
                                //System.out.println("                        MarketplaceASIN");
                                //System.out.println();
                                ASINIdentifier  marketplaceASIN = identifiers.getMarketplaceASIN();
                                if (marketplaceASIN.isSetMarketplaceId()) {
                                    //System.out.println("                            MarketplaceId");
                                    //System.out.println();
                                    //System.out.println("                                " + marketplaceASIN.getMarketplaceId());
                                    //System.out.println();
                                }
                                if (marketplaceASIN.isSetASIN()) {
                                //    System.out.println("                            ASIN");
                                //    System.out.println();
                                //    System.out.println("                                " + marketplaceASIN.getASIN());
                                //    System.out.println();
                                	
                                	asin = marketplaceASIN.getASIN();
                                	
                                	System.out.print(asin + "\t");
                            //  	    outFile.write(asin + "\t");
                              	    
                                    //System.out.println();
                                    //outFile.newLine();
                                     
                                }
                            } 
                            if (identifiers.isSetSKUIdentifier()) {
                                //System.out.println("                        SKUIdentifier");
                                //System.out.println();
                                SellerSKUIdentifier  SKUIdentifier = identifiers.getSKUIdentifier();
                                if (SKUIdentifier.isSetMarketplaceId()) {
                                    //System.out.println("                            MarketplaceId");
                                    //System.out.println();
                                    //System.out.println("                                " + SKUIdentifier.getMarketplaceId());
                                    //System.out.println();
                                }
                                if (SKUIdentifier.isSetSellerId()) {
                                    //System.out.println("                            SellerId");
                                    //System.out.println();
                                    //System.out.println("                                " + SKUIdentifier.getSellerId());
                                    //System.out.println();
                                }
                                if (SKUIdentifier.isSetSellerSKU()) {
                                    //System.out.println("                            SellerSKU");
                                    //System.out.println();
                                    //System.out.println("                                " + SKUIdentifier.getSellerSKU());
                                    //System.out.println();
                                }
                            } 
                        } 
                        if (product.isSetAttributeSets()) {
                          //System.out.println("                    Attributes");
          
                          AttributeSetList attributeSetList = product.getAttributeSets();
                          for (Object obj : attributeSetList.getAny()) {
//                             Node attribute = (Node) obj;
//                              String fxml = ProductsUtil.formatXml(attribute);
//                              System.out.print(fxml); 
                             // outFile.write(fxml);  
                          }
                        }
                        if (product.isSetRelationships()) {
                          //System.out.println("                    Relationships");
                          RelationshipList relationships = product.getRelationships();
                          for (Object obj : relationships.getAny()) {
                             //Node relationship = (Node) obj;
                             //System.out.println(ProductsUtil.formatXml(relationship));
                          }
                          //System.out.println();
                        }
                        if (product.isSetCompetitivePricing()) {
                            //System.out.println("                    CompetitivePricing");
                            //System.out.println();
                            CompetitivePricingType  competitivePricing = product.getCompetitivePricing();
                            if (competitivePricing.isSetCompetitivePrices()) {
                                //System.out.println("                        CompetitivePrices");
                                //System.out.println();
                                CompetitivePriceList  competitivePrices = competitivePricing.getCompetitivePrices();
                                java.util.List<CompetitivePriceType> competitivePriceList = competitivePrices.getCompetitivePrice();
                                for (CompetitivePriceType competitivePrice : competitivePriceList) {
                                    //System.out.println("                            CompetitivePrice");
                                    //System.out.println();
                                if (competitivePrice.isSetCondition()) {
                                    //System.out.println("                            condition");
                                    //System.out.println();
                                    //System.out.println("                                " + competitivePrice.getCondition());
                                    //System.out.println();
                                } 
                                if (competitivePrice.isSetSubcondition()) {
                                    //System.out.println("                            subcondition");
                                    //System.out.println();
                                    //System.out.println("                                " + competitivePrice.getSubcondition());
                                    //System.out.println();
                                } 
                                if (competitivePrice.isSetBelongsToRequester()) {
                                    //System.out.println("                            belongsToRequester");
                                    //System.out.println();
                                    //System.out.println("                                " + competitivePrice.isBelongsToRequester());
                                    //System.out.println();
                                } 
                                    if (competitivePrice.isSetCompetitivePriceId()) {
                                        //System.out.println("                                CompetitivePriceId");
                                        //System.out.println();
                                        //System.out.println("                                    " + competitivePrice.getCompetitivePriceId());
                                        //System.out.println();
                                    }
                                    if (competitivePrice.isSetPrice()) {
                                        //System.out.println("                                Price");
                                        //System.out.println();
                                        PriceType  price = competitivePrice.getPrice();
                                        if (price.isSetLandedPrice()) {
                                            //System.out.println("                                    LandedPrice");
                                            //System.out.println();
                                            MoneyType  landedPrice = price.getLandedPrice();
                                            if (landedPrice.isSetCurrencyCode()) {
                                                //System.out.println("                                        CurrencyCode");
                                                //System.out.println();
                                                //System.out.println("                                            " + landedPrice.getCurrencyCode());
                                                //System.out.println();
                                            }
                                            if (landedPrice.isSetAmount()) {
                                                //System.out.println("                                        Amount");
                                                //System.out.println();
                                                //System.out.println("                                            " + landedPrice.getAmount());
                                                //System.out.println();
                                            }
                                        } 
                                        if (price.isSetListingPrice()) {
                                            //System.out.println("                                    ListingPrice");
                                            //System.out.println();
                                            MoneyType  listingPrice = price.getListingPrice();
                                            if (listingPrice.isSetCurrencyCode()) {
                                                //System.out.println("                                        CurrencyCode");
                                                //System.out.println();
                                                //System.out.println("                                            " + listingPrice.getCurrencyCode());
                                                //System.out.println();
                                            }
                                            if (listingPrice.isSetAmount()) {
                                                //System.out.println("                                        Amount");
                                                //System.out.println();
                                                //System.out.println("                                            " + listingPrice.getAmount());
                                                //System.out.println();
                                            }
                                        } 
                                        if (price.isSetShipping()) {
                                            //System.out.println("                                    Shipping");
                                            //System.out.println();
                                            MoneyType  shipping = price.getShipping();
                                            if (shipping.isSetCurrencyCode()) {
                                                //System.out.println("                                        CurrencyCode");
                                                //System.out.println();
                                                //System.out.println("                                            " + shipping.getCurrencyCode());
                                                //System.out.println();
                                            }
                                            if (shipping.isSetAmount()) {
                                                //System.out.println("                                        Amount");
                                                //System.out.println();
                                                //System.out.println("                                            " + shipping.getAmount());
                                                //System.out.println();
                                            }
                                        } 
                                    } 
                                }
                            } 
                            if (competitivePricing.isSetNumberOfOfferListings()) {
                                //System.out.println("                        NumberOfOfferListings");
                                //System.out.println();
                                NumberOfOfferListingsList  numberOfOfferListings = competitivePricing.getNumberOfOfferListings();
                                java.util.List<OfferListingCountType> offerListingCountList = numberOfOfferListings.getOfferListingCount();
                                for (OfferListingCountType offerListingCount : offerListingCountList) {
                                    //System.out.println("                            OfferListingCount");
                                    //System.out.println();
                                if (offerListingCount.isSetCondition()) {
                                    //System.out.println("                            condition");
                                    //System.out.println();
                                    //System.out.println("                                " + offerListingCount.getCondition());
                                    //System.out.println();
                                } 
                                if (offerListingCount.isSetValue()) {
                                    //System.out.println("                            Value");
                                    //System.out.println();
                                    //System.out.println("                                " + offerListingCount.getValue());
                                } 
                                }
                            } 
                            if (competitivePricing.isSetTradeInValue()) {
                                //System.out.println("                        TradeInValue");
                                //System.out.println();
                                MoneyType  tradeInValue = competitivePricing.getTradeInValue();
                                if (tradeInValue.isSetCurrencyCode()) {
                                    //System.out.println("                            CurrencyCode");
                                    //System.out.println();
                                    //System.out.println("                                " + tradeInValue.getCurrencyCode());
                                    //System.out.println();
                                }
                                if (tradeInValue.isSetAmount()) {
                                    //System.out.println("                            Amount");
                                    //System.out.println();
                                    //System.out.println("                                " + tradeInValue.getAmount());
                                    //System.out.println();
                                }
                            } 
                        } 
                        
                        int salesrank = 0;
                        int rankcate = 1;
                        
                        if (product.isSetSalesRankings()) {
                            //System.out.println("                    SalesRankings");
                            //System.out.println();
                            SalesRankList  salesRankings = product.getSalesRankings();
                            java.util.List<SalesRankType> salesRankList = salesRankings.getSalesRank();
                            for (SalesRankType salesRank : salesRankList) {
                                //System.out.println("                        SalesRank");
                                //System.out.println();
                                if (salesRank.isSetProductCategoryId()) {
                                    //System.out.println("                            ProductCategoryId");
                                    //System.out.println();
                                    //System.out.println("                                " + salesRank.getProductCategoryId());
                                    //System.out.println();
                                	System.out.print(salesRank.getProductCategoryId()+ "\t"); 
                                //    outFile.write(salesRank.getProductCategoryId()+ "\t");   

                                }
                                else
                                {
                                //	outFile.write("NULL \t"); 
                                }
                                if (salesRank.isSetRank()) 
                                {
                                    
                                	//System.out.println("                            Rank");
                                    //System.out.println();
                                    //System.out.println("                                " + salesRank.getRank());
                                    //System.out.println();
                                	
                                	System.out.print(salesRank.getRank() + "\t"); 
                                //    outFile.write(salesRank.getRank() + "\t");  
                                    my_db.setSalesRank(asin,salesRank.getRank());
                                    if (rankcate == 1)
                                    {
                                    	salesrank = salesRank.getRank() ;
                                    	break;
                                    }
                                    rankcate++;
                                }
                                else
                                {
                                //	outFile.write("NULL \t"); 
                                }
                            }
                            if (salesRankList.size() == 0)
                            {
                            //	outFile.write("NULL \t"); 
                            //	outFile.write("NULL \t"); 
                            }
                        }
                        else
                        {
                        //	outFile.write("NULL \t"); 
                        //	outFile.write("NULL \t"); 
                        }
                        
                        System.out.println();
                        //outFile.newLine();

                    	//my_db.setSalesRank(asin, salesrank);
                    	
                        if (product.isSetLowestOfferListings()) {
                            //System.out.println("                    LowestOfferListings");
                            //System.out.println();
                            LowestOfferListingList  lowestOfferListings = product.getLowestOfferListings();
                            java.util.List<LowestOfferListingType> lowestOfferListingList = lowestOfferListings.getLowestOfferListing();
                            for (LowestOfferListingType lowestOfferListing : lowestOfferListingList) {
                                //System.out.println("                        LowestOfferListing");
                                //System.out.println();
                                if (lowestOfferListing.isSetQualifiers()) {
                                    //System.out.println("                            Qualifiers");
                                    //System.out.println();
                                    QualifiersType  qualifiers = lowestOfferListing.getQualifiers();
                                    if (qualifiers.isSetItemCondition()) {
                                        //System.out.println("                                ItemCondition");
                                        //System.out.println();
                                        //System.out.println("                                    " + qualifiers.getItemCondition());
                                        //System.out.println();
                                    }
                                    if (qualifiers.isSetItemSubcondition()) {
                                        //System.out.println("                                ItemSubcondition");
                                        //System.out.println();
                                        //System.out.println("                                    " + qualifiers.getItemSubcondition());
                                        //System.out.println();
                                    }
                                    if (qualifiers.isSetFulfillmentChannel()) {
                                        //System.out.println("                                FulfillmentChannel");
                                        //System.out.println();
                                        //System.out.println("                                    " + qualifiers.getFulfillmentChannel());
                                        //System.out.println();
                                    }
                                    if (qualifiers.isSetShipsDomestically()) {
                                        //System.out.println("                                ShipsDomestically");
                                        //System.out.println();
                                        //System.out.println("                                    " + qualifiers.getShipsDomestically());
                                        //System.out.println();
                                    }
                                    if (qualifiers.isSetShippingTime()) {
                                        //System.out.println("                                ShippingTime");
                                        //System.out.println();
                                        ShippingTimeType  shippingTime = qualifiers.getShippingTime();
                                        if (shippingTime.isSetMax()) {
                                            //System.out.println("                                    Max");
                                            //System.out.println();
                                            //System.out.println("                                        " + shippingTime.getMax());
                                            //System.out.println();
                                        }
                                    } 
                                    if (qualifiers.isSetSellerPositiveFeedbackRating()) {
                                        //System.out.println("                                SellerPositiveFeedbackRating");
                                        //System.out.println();
                                        //System.out.println("                                    " + qualifiers.getSellerPositiveFeedbackRating());
                                        //System.out.println();
                                    }
                                } 
                                if (lowestOfferListing.isSetNumberOfOfferListingsConsidered()) {
                                    //System.out.println("                            NumberOfOfferListingsConsidered");
                                    //System.out.println();
                                    //System.out.println("                                " + lowestOfferListing.getNumberOfOfferListingsConsidered());
                                    //System.out.println();
                                }
                                if (lowestOfferListing.isSetSellerFeedbackCount()) {
                                    //System.out.println("                            SellerFeedbackCount");
                                    //System.out.println();
                                    //System.out.println("                                " + lowestOfferListing.getSellerFeedbackCount());
                                    //System.out.println();
                                }
                                if (lowestOfferListing.isSetPrice()) {
                                    //System.out.println("                            Price");
                                    //System.out.println();
                                    PriceType  price1 = lowestOfferListing.getPrice();
                                    if (price1.isSetLandedPrice()) {
                                        //System.out.println("                                LandedPrice");
                                        //System.out.println();
                                        MoneyType  landedPrice1 = price1.getLandedPrice();
                                        if (landedPrice1.isSetCurrencyCode()) {
                                            //System.out.println("                                    CurrencyCode");
                                            //System.out.println();
                                            //System.out.println("                                        " + landedPrice1.getCurrencyCode());
                                            //System.out.println();
                                        }
                                        if (landedPrice1.isSetAmount()) {
                                            //System.out.println("                                    Amount");
                                            //System.out.println();
                                            //System.out.println("                                        " + landedPrice1.getAmount());
                                            //System.out.println();
                                        }
                                    } 
                                    if (price1.isSetListingPrice()) {
                                        //System.out.println("                                ListingPrice");
                                        //System.out.println();
                                        MoneyType  listingPrice1 = price1.getListingPrice();
                                        if (listingPrice1.isSetCurrencyCode()) {
                                            //System.out.println("                                    CurrencyCode");
                                            //System.out.println();
                                            //System.out.println("                                        " + listingPrice1.getCurrencyCode());
                                            //System.out.println();
                                        }
                                        if (listingPrice1.isSetAmount()) {
                                            //System.out.println("                                    Amount");
                                            //System.out.println();
                                            //System.out.println("                                        " + listingPrice1.getAmount());
                                            //System.out.println();
                                        }
                                    } 
                                    if (price1.isSetShipping()) {
                                        //System.out.println("                                Shipping");
                                        //System.out.println();
                                        MoneyType  shipping1 = price1.getShipping();
                                        if (shipping1.isSetCurrencyCode()) {
                                            //System.out.println("                                    CurrencyCode");
                                            //System.out.println();
                                            //System.out.println("                                        " + shipping1.getCurrencyCode());
                                            //System.out.println();
                                        }
                                        if (shipping1.isSetAmount()) {
                                            //System.out.println("                                    Amount");
                                            //System.out.println();
                                            //System.out.println("                                        " + shipping1.getAmount());
                                            //System.out.println();
                                        }
                                    } 
                                } 
                                if (lowestOfferListing.isSetMultipleOffersAtLowestPrice()) {
                                    //System.out.println("                            MultipleOffersAtLowestPrice");
                                    //System.out.println();
                                    //System.out.println("                                " + lowestOfferListing.getMultipleOffersAtLowestPrice());
                                    //System.out.println();
                                }
                            }
                        } 
                        if (product.isSetOffers()) {
                            //System.out.println("                    Offers");
                            //System.out.println();
                            OffersList  offers = product.getOffers();
                            java.util.List<OfferType> offerList = offers.getOffer();
                            for (OfferType offer : offerList) {
                                //System.out.println("                        Offer");
                                //System.out.println();
                                if (offer.isSetBuyingPrice()) {
                                    //System.out.println("                            BuyingPrice");
                                    //System.out.println();
                                    PriceType  buyingPrice = offer.getBuyingPrice();
                                    if (buyingPrice.isSetLandedPrice()) {
                                        //System.out.println("                                LandedPrice");
                                        //System.out.println();
                                        MoneyType  landedPrice2 = buyingPrice.getLandedPrice();
                                        if (landedPrice2.isSetCurrencyCode()) {
                                            //System.out.println("                                    CurrencyCode");
                                            //System.out.println();
                                            //System.out.println("                                        " + landedPrice2.getCurrencyCode());
                                            //System.out.println();
                                        }
                                        if (landedPrice2.isSetAmount()) {
                                            //System.out.println("                                    Amount");
                                            //System.out.println();
                                            //System.out.println("                                        " + landedPrice2.getAmount());
                                            //System.out.println();
                                        }
                                    } 
                                    if (buyingPrice.isSetListingPrice()) {
                                        //System.out.println("                                ListingPrice");
                                        //System.out.println();
                                        MoneyType  listingPrice2 = buyingPrice.getListingPrice();
                                        if (listingPrice2.isSetCurrencyCode()) {
                                            //System.out.println("                                    CurrencyCode");
                                            //System.out.println();
                                            //System.out.println("                                        " + listingPrice2.getCurrencyCode());
                                            //System.out.println();
                                        }
                                        if (listingPrice2.isSetAmount()) {
                                            //System.out.println("                                    Amount");
                                            //System.out.println();
                                            //System.out.println("                                        " + listingPrice2.getAmount());
                                            //System.out.println();
                                        }
                                    } 
                                    if (buyingPrice.isSetShipping()) {
                                        //System.out.println("                                Shipping");
                                        //System.out.println();
                                        MoneyType  shipping2 = buyingPrice.getShipping();
                                        if (shipping2.isSetCurrencyCode()) {
                                            //System.out.println("                                    CurrencyCode");
                                            //System.out.println();
                                            //System.out.println("                                        " + shipping2.getCurrencyCode());
                                            //System.out.println();
                                        }
                                        if (shipping2.isSetAmount()) {
                                            //System.out.println("                                    Amount");
                                            //System.out.println();
                                            //System.out.println("                                        " + shipping2.getAmount());
                                            //System.out.println();
                                        }
                                    } 
                                } 
                                if (offer.isSetRegularPrice()) {
                                    //System.out.println("                            RegularPrice");
                                    //System.out.println();
                                    MoneyType  regularPrice = offer.getRegularPrice();
                                    if (regularPrice.isSetCurrencyCode()) {
                                        //System.out.println("                                CurrencyCode");
                                        //System.out.println();
                                        //System.out.println("                                    " + regularPrice.getCurrencyCode());
                                        //System.out.println();
                                    }
                                    if (regularPrice.isSetAmount()) {
                                        //System.out.println("                                Amount");
                                        //System.out.println();
                                        //System.out.println("                                    " + regularPrice.getAmount());
                                        //System.out.println();
                                    }
                                } 
                                if (offer.isSetFulfillmentChannel()) {
                                    //System.out.println("                            FulfillmentChannel");
                                    //System.out.println();
                                    //System.out.println("                                " + offer.getFulfillmentChannel());
                                    //System.out.println();
                                }
                                if (offer.isSetItemCondition()) {
                                    //System.out.println("                            ItemCondition");
                                    //System.out.println();
                                    //System.out.println("                                " + offer.getItemCondition());
                                    //System.out.println();
                                }
                                if (offer.isSetItemSubCondition()) {
                                    //System.out.println("                            ItemSubCondition");
                                    //System.out.println();
                                    //System.out.println("                                " + offer.getItemSubCondition());
                                    //System.out.println();
                                }
                                if (offer.isSetSellerId()) {
                                    //System.out.println("                            SellerId");
                                    //System.out.println();
                                    //System.out.println("                                " + offer.getSellerId());
                                    //System.out.println();
                                }
                                if (offer.isSetSellerSKU()) {
                                    //System.out.println("                            SellerSKU");
                                    //System.out.println();
                                    //System.out.println("                                " + offer.getSellerSKU());
                                    //System.out.println();
                                }
                            }
                        } 
                    }
                } 
                if (getMatchingProductForIdResult.isSetError()) {
                    //System.out.println("            Error");
                    //System.out.println();
                    com.amazonservices.mws.products.model.Error  error = getMatchingProductForIdResult.getError();
                    if (error.isSetType()) {
                        //System.out.println("                Type");
                        //System.out.println();
                        //System.out.println("                    " + error.getType());
                        //System.out.println();
                    }
                    if (error.isSetCode()) {
                        //System.out.println("                Code");
                        //System.out.println();
                        //System.out.println("                    " + error.getCode());
                        //System.out.println();
                    }
                    if (error.isSetMessage()) {
                        //System.out.println("                Message");
                        //System.out.println();
                        //System.out.println("                    " + error.getMessage());
                        //System.out.println();
                    }
                } 
            }
            if (response.isSetResponseMetadata()) {
                //System.out.println("        ResponseMetadata");
                //System.out.println();
                ResponseMetadata  responseMetadata = response.getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    //System.out.println("            RequestId");
                    //System.out.println();
                    //System.out.println("                " + responseMetadata.getRequestId());
                    //System.out.println();
                }
            } 
            //System.out.println();
            //System.out.println(response.getResponseHeaderMetadata());
            //System.out.println();

           
        } catch (MarketplaceWebServiceProductsException ex) {
            
            //System.out.println("Caught Exception: " + ex.getMessage());
            //System.out.println("Response Status Code: " + ex.getStatusCode());
            //System.out.println("Error Code: " + ex.getErrorCode());
            //System.out.println("Error Type: " + ex.getErrorType());
            //System.out.println("Request ID: " + ex.getRequestId());
            //System.out.println("XML: " + ex.getXML());
            //System.out.print("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
        }
    }
                                    
}

