package cscie97.asn4.ecommerce.product;

import java.io.*;
import java.util.*;

import cscie97.asn4.ecommerce.authentication.*;

/**
 * {@code ProductCatalog} maintains and provides access to Country, Device, and Content data.
 */
public class ProductCatalog implements Catalog{
   
    // The active set of valid Countries
    private Map<String, Country> countryMap;
   
    // The active set of valid Devices
    private Map<String, Device> deviceMap;
   
    // The active set of valid Content
    private Map<String, Content> contentMap;
   
    // The lookup maps for search criteria
    private Map<String, Set<Content>> searchCatMap;
    private Map<String, Set<Content>> searchTextMap;
    private Map<Integer, Set<Content>> searchRatingMap;
    private Map<Integer, Set<Content>> searchPriceMap;
    private Map<String, Set<Content>> searchLangMap;
    private Map<String, Set<Content>> searchCountryMap;
    private Map<String, Set<Content>> searchDeviceMap;
    private Map<String, Set<Content>> searchTypeMap;

    /**
     * Private ProductCatalog constructor
     */
    private ProductCatalog(){
        this.countryMap = new TreeMap<String, Country>();
        this.deviceMap = new TreeMap<String, Device>();
        this.contentMap = new TreeMap<String, Content>();
        this.searchCatMap = new TreeMap<String, Set<Content>>();
        this.searchTextMap = new TreeMap<String, Set<Content>>();
        this.searchRatingMap = new TreeMap<Integer, Set<Content>>();
        // Initialize searchRatingMap with keys from 0 - 5, which are the possible ratings
        for(int i = 0; i <= 5; i++){
            searchRatingMap.put(i, null);
        }
        this.searchPriceMap = new TreeMap<Integer, Set<Content>>();
        this.searchLangMap = new TreeMap<String, Set<Content>>();
        this.searchCountryMap = new TreeMap<String, Set<Content>>();
        this.searchDeviceMap = new TreeMap<String, Set<Content>>();
        this.searchTypeMap = new TreeMap<String, Set<Content>>();
    }
   
    /**
     * {@code ProductCatalogSingleton} is loaded on the first execution of
     * ProductCatalog.getInstance() (Bill Pugh's technique).
     *
     */
    private static class ProductCatalogSingleton{
        public static final ProductCatalog pc = new ProductCatalog();
    }
   
    /**
     * Creates singleton instance of ProductCatalog
     *
     * @return single static instance of ProductCatalog
     */
    public static ProductCatalog getInstance(){
        return ProductCatalogSingleton.pc;
    }   

    @Override
    public void importCSV(String fileName, String dataType, String accessToken) 
    	throws ImportException, PermissionException, InvalidAccessTokenException {
		try{
	    	AuthenticationServiceAPI as = AuthenticationServiceAPI.getInstance();
			String perm = "product_import";
			boolean hasAccess = as.verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", null, perm);
			}			
		} catch(InvalidAccessTokenException e){
			throw e;
		}          
    	try{           
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
           
            int lineNum = 0;
            String line;
            String delims = "(?<!\\\\),"; // negative lookbehind regex for comma not preceded by backslash
            String[] parsed;
           
            dataType = (dataType.toLowerCase()).trim();
           
            if(dataType.equals("country")){
                try{
                    while((line = br.readLine()) != null){
                        // skip blank lines
                        if((line.trim()).equals("")){
                            continue;
                        }
                        parsed = line.split(delims);
                        lineNum++;
                        if(parsed.length != 3){
                            throw new ImportException("Error Importing: Country data does not have correct " +
                            						  "number (3) of fields", line, fileName);
                        }
                        if(lineNum == 1){ // check additional header line for asn3
                        	continue;
                        }	
                        else if(lineNum == 2){ // check header
                            String headId = ((parsed[0].replace("#", "")).toLowerCase()).trim();
                            String headName = (parsed[1].toLowerCase()).trim();
                            String headStatus = (parsed[2].toLowerCase()).trim();
                           
                            if(!headId.equals("country_id") && !headName.equals("country_name") &&
                               !headStatus.equals("country_export_status")){     
                                throw new ImportException("Error Importing: Country fields are not in " +
                                						  "standard order of country_id, country_name, and " + 
                                						  "country_export_status.", line, fileName);
                            }
                        }
                        else{
                            addCountry(accessToken, parsed);
                        }
                    }
                    if(lineNum == 2){
                        System.out.println("There was no Country data to import!");
                    }
                }
                catch(CatalogException ce){
                	System.out.println("Failure adding " + ce.getDataType() + " data: " + ce.getData());
                    System.out.println(ce.getMessage());
        			System.exit(1);
                }
            }
            else if(dataType.equals("device")){
                while((line = br.readLine()) != null){
                    // skip blank lines
                    if((line.trim()).equals("")){
                        continue;
                    }
                    parsed = line.split(delims);
                    lineNum++;
                    if(parsed.length != 3){
                        throw new ImportException("Error Importing: Device data does not have correct " +
                        					      "number (3) of fields", line, fileName);
                    }
                    if(lineNum == 1){ // check additional header line for asn3
                    	continue;
                    }	
                    else if(lineNum == 2){ // check header
                        String headId = ((parsed[0].replace("#", "")).toLowerCase()).trim();
                        String headName = (parsed[1].toLowerCase()).trim();
                        String headStatus = (parsed[2].toLowerCase()).trim();
                       
                        if(!headId.equals("device_id") && !headName.equals("device_name") &&
                           !headStatus.equals("manufacturer")){
                            throw new ImportException("Error Importing: Device fields are not in standard " +
                            						  "order of device_id, device_name, and manufacturer",
                                                      line, fileName);
                        }
                    }
                    else{
                        addDevice(accessToken, parsed);
                    }
                }
                if(lineNum == 1){
                    System.out.println("There was no Device data to import!");
                }              
            }
            else if(dataType.equals("content")){
                try{
                    while((line = br.readLine()) != null){
                        // skip blank lines
                        if((line.trim()).equals("")){
                            continue;
                        }
                        parsed = line.split(delims);
                        lineNum++;
                        if((lineNum == 2 && parsed.length != 13) ||
                           (lineNum != 1 && parsed.length != 13 && parsed.length != 12)){ 
                            throw new ImportException("Error Importing: Content does not have correct " +
                            						  "number of fields", line, fileName);
                        }
                        if(lineNum == 1){ // check additional header line for asn3
                        	continue;
                        }	
                        if(lineNum == 2){ // check header
                            String headType = ((parsed[0].replace("#", "")).toLowerCase()).trim();
                            String headId = (parsed[1].toLowerCase()).trim();
                            String headName = (parsed[2].toLowerCase()).trim();
                            String headDesc= (parsed[3].toLowerCase()).trim();
                            String headAuth = (parsed[4].toLowerCase()).trim();
                            String headRating = (parsed[5].toLowerCase()).trim();
                            String headCat = (parsed[5].toLowerCase()).trim();
                            String headCountries = (parsed[5].toLowerCase()).trim();
                            String headDevices = (parsed[5].toLowerCase()).trim();
                            String headPrice = (parsed[5].toLowerCase()).trim();
                            String headLangs = (parsed[5].toLowerCase()).trim();
                            String headUrl = (parsed[5].toLowerCase()).trim();
                            String headSize = (parsed[5].toLowerCase()).trim();
                            if(!headType.equals("content_type") && !headId.equals("content_id") &&
                               !headName.equals("content_name") && !headDesc.equals("content_description") &&
                               !headAuth.equals("author") && !headRating.equals("rating") &&
                               !headCat.equals("categories") && !headCountries.equals("export_countries") &&
                               !headDevices.equals("supported_devices") && !headPrice.equals("price") &&
                               !headLangs.equals("supported_languages") && !headUrl.equals("image_url") &&
                               !headSize.equals("application_size")){
                                throw new ImportException("Error Importing: Content fields are not in " +
                                						  "standard order of content_type, content_id, " +
                                						  "content_name, content_description, " +
                                                          "author, rating, categories, export_countries, " +
                                                          "supported_devices, price, supported_languages, " +
                                                          "image_url, and application_size",
                                                          line, fileName);
                            }
                        }
                        else{
                            String type = (parsed[0].toLowerCase()).trim();
                            addContent(accessToken, parsed, type);
                        }
                    }
                    if(lineNum == 1){
                        System.out.println("There was no Content data to import!");
                    }
                }
                catch(CatalogException ce){
                	System.out.println("Failure adding " + ce.getDataType() + " data: " + ce.getData());
                    System.out.println(ce.getMessage());
        			System.exit(1);
                }                               
            }
            else{
                throw new ImportException("Error Importing: " + dataType + " is not a currently supported " +
                						  "type of data for import", null, fileName);
            }
        }
        catch(FileNotFoundException fe){
            System.out.println(fe.getMessage());
            System.exit(1);
        }
        catch(IOException ioe){
            System.out.println(ioe.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void executeSearchFile(String fileName) throws SearchEngineException{
        try{           
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
           
            int lineNum = 0;
            String line;
            String delims = "(?<!\\\\),"; // negative lookbehind regex for comma not preceded by backslash
            String[] parsed;
             
            while((line = br.readLine()) != null){
                // skip blank lines
                if((line.trim()).equals("")){
                    continue;
                }
                parsed = line.split(delims);
                lineNum++;
                if(parsed.length != 8 && ((!(parsed[0].trim()).startsWith("#") && 
                   lineNum > 2) || lineNum == 2)){
                	System.out.println(parsed.length);
                	System.out.println(line);
                    throw new SearchEngineException("Search Error: Search data does not have correct " +
                    								"number (8) of criteria fields", line, fileName);
                }
                if(lineNum == 1){ // check additional header line for asn3
                	continue;
                }	
                if(lineNum == 2){ // check header
                    String headCat = ((parsed[0].replace("#", "")).toLowerCase()).trim();
                    String headText = (parsed[1].toLowerCase()).trim();
                    String headRating = (parsed[2].toLowerCase()).trim();
                    String headPrice = (parsed[3].toLowerCase()).trim();
                    String headLang = (parsed[4].toLowerCase()).trim();
                    String headCtry = (parsed[5].toLowerCase()).trim();
                    String headDev = (parsed[6].toLowerCase()).trim();
                    String headContent = (parsed[7].toLowerCase()).trim();
                    
                    if(!headCat.equals("category list") && !headText.equals("text search") &&
                       !headRating.equals("minimum rating") && !headPrice.equals("max price") &&
                       !headLang.equals("language list") && !headCtry.equals("country code") &&
                       !headDev.equals("device id") && !headContent.equals("content type list")){
                        throw new SearchEngineException("Search fields are not in standard order of " +
                                                        "category list, text search, minimum rating, " +
                                                        "max price, language list, country_code, " +
                                                        "device_id, and content type list.",
                                                        line, fileName);
                    }
                }
                else{
                    // print out query description
                    if((parsed[0].trim()).startsWith("#")){
                        System.out.println(parsed[0].trim());
                    }
                    // send search strings to executeSearch and display results
                    else{
                        Set<Content> results = executeSearch(parsed);
                        if(results != null){
                            displayResults(results);
                        }
                        else{
                            System.out.println("There were no results for the provided criteria.");                       
                        }
         
                    }
                }
            }
            if(lineNum == 1){
                System.out.println("There were no searches to execute!");
            }       
        }
        catch(SearchEngineException see){
        	System.out.println("Error processing search in file " + fileName);
            System.out.println(see.getMessage());
			System.exit(1);
        }
        catch(FileNotFoundException fe){
            System.out.println(fe.getMessage());
            System.exit(1);
        }
        catch(IOException ioe){
            System.out.println(ioe.getMessage());
            System.exit(1);
        }
    }
   
    /**
     * Creates new Country and adds to country map if it doesn't already exist.
     * 
     * @param accessToken token restricting access to add data
     * @param countryData Country data
     * @throws CatalogException when an error is encountered creating the Country
     */
    private void addCountry(String accessToken, String[] countryData) 
    	throws CatalogException, PermissionException, InvalidAccessTokenException{
		try{
	    	AuthenticationServiceAPI as = AuthenticationServiceAPI.getInstance();
			String perm = "create_country";
			boolean hasAccess = as.verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", null, perm);
			}			
		} catch(InvalidAccessTokenException e){
			throw e;
		}           
    	String code;
        String name;
        String status;

        code = (countryData[0].toUpperCase()).trim();
        name = countryData[1].trim();
        status = (countryData[2].toLowerCase()).trim();

        if(!status.equals("open") && !status.equals("closed")){
            throw new CatalogException("Error adding to Catalog: Invalid export status", "Country", status);
        }
       
        if(this.countryMap.get(code) == null){
            Country c = new Country(code, name, status);
            this.countryMap.put(code, c);
            System.out.println("Country " + code + " was added!");
        }
        else{
            System.out.println("Country " + code + " is already in system.");
        }
    }
   
    /**
     * Creates new Device and adds to device map if it doesn't already exist.
     * 
     * @param accessToken token restricting access to add data
     * @param deviceData Device data
     */
    private void addDevice(String accessToken, String[] deviceData)
    	throws PermissionException, InvalidAccessTokenException{
		try{
	    	AuthenticationServiceAPI as = AuthenticationServiceAPI.getInstance();
			String perm = "create_device";
			boolean hasAccess = as.verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", null, perm);
			}			
		} catch(InvalidAccessTokenException e){
			throw e;
		}           
    	String id;
        String name;
        String manufacturer;

        id = (deviceData[0].toLowerCase()).trim();
        name = deviceData[1].trim();
        manufacturer = (deviceData[2].toLowerCase()).trim();
           
        if(this.deviceMap.get(id) == null){
            Device d = new Device(id, name, manufacturer);
            this.deviceMap.put(id, d);
            System.out.println("Device " + id + " was added!");
        }
        else{
            System.out.println("Device " + id + " is already in system.");
        }    
    }
   
    /**
     * Adds Content to the search maps.
     * 
     * @param c Content to add to search maps
     * @param type type of Content being added to search maps
     */
    private void updateSearchMaps(Content c, String type){     
        // update category search map
        Iterator<String> itCat = c.getCategories().iterator();
        while(itCat.hasNext()){
            Set<Content> setCat = new HashSet<Content>();
            String keyCat = itCat.next();
            if(this.searchCatMap.get(keyCat) == null || !this.searchCatMap.containsKey(keyCat)){
                setCat.add(c);
                this.searchCatMap.put(keyCat, setCat);
            }
            else{
                this.searchCatMap.get(keyCat).add(c);
            }
        }
        // update text search map
        // remove punctuation and consider words as separated by whitespace
        String[] parsedName = c.getName().replaceAll("\\p{P}", "").split(" +"); 
        for(int i = 0; i < parsedName.length; i++){
            Set<Content> setName = new HashSet<Content>();
            String keyName = parsedName[i].trim().toLowerCase();
            if(this.searchTextMap.get(keyName) == null || !this.searchTextMap.containsKey(keyName)){
                setName.add(c);
                this.searchTextMap.put(keyName, setName);
            }
            else{
                this.searchTextMap.get(keyName).add(c);
            }
        }   
        String[] parsedDesc = c.getDescription().replaceAll("\\p{P}","").split(" +");
        for(int i = 0; i < parsedDesc.length; i++){
            Set<Content> setDesc = new HashSet<Content>();  
            String keyDesc = parsedDesc[i].trim().toLowerCase();
            if(this.searchTextMap.get(keyDesc) == null || !this.searchTextMap.containsKey(keyDesc)){
                setDesc.add(c);
                this.searchTextMap.put(keyDesc, setDesc);
            }
            else{
                this.searchTextMap.get(keyDesc).add(c);
            }
        }
        // update rating search map
        Set<Content> setRating = new HashSet<Content>();
        int rating = c.getRating();
        if(this.searchRatingMap.get(rating) == null || !this.searchRatingMap.containsKey(rating)){
        	setRating.add(c);
            this.searchRatingMap.put(rating, setRating);
        }
        else{
            this.searchRatingMap.get(rating).add(c);
        }
        
        // update price search map
        Set<Content> setPrice = new HashSet<Content>();       
        int keyPrice = Math.round(c.getPrice());
        if(this.searchPriceMap.get(keyPrice) == null || !this.searchPriceMap.containsKey(keyPrice)){
            setPrice.add(c);
            this.searchPriceMap.put(keyPrice, setPrice);
        }
        else{
            this.searchPriceMap.get(keyPrice).add(c);
        } 
        // update language search map
        Iterator<String> itLang = c.getLanguages().iterator();
        while(itLang.hasNext()){
            Set<Content> setLang = new HashSet<Content>();
            String keyLang = itLang.next();
            if(this.searchLangMap.get(keyLang) == null || !this.searchLangMap.containsKey(keyLang)){
                setLang.add(c);
                this.searchLangMap.put(keyLang, setLang);
            }
            else{
                this.searchLangMap.get(keyLang).add(c);
            }
        }
        // update country search map
        Iterator<Country> itCtry = c.getCountries().iterator();
        while(itCtry.hasNext()){
            Set<Content> setCtry = new HashSet<Content>();
            String keyCtry = itCtry.next().getCountryCode();
            if(this.searchCountryMap.get(keyCtry) == null || !this.searchCountryMap.containsKey(keyCtry)){
                setCtry.add(c);
                this.searchCountryMap.put(keyCtry, setCtry);
            }
            else{
                this.searchCountryMap.get(keyCtry).add(c);
            }
        }
        // update device search map
        Iterator<Device> itDev = c.getDevices().iterator();
        while(itDev.hasNext()){
            Set<Content> setDev = new HashSet<Content>();
            String keyDev = itDev.next().getDeviceId();
            if(this.searchDeviceMap.get(keyDev) == null || !this.searchDeviceMap.containsKey(keyDev)){
                setDev.add(c);
                this.searchDeviceMap.put(keyDev, setDev);
            }
            else{
                this.searchDeviceMap.get(keyDev).add(c);
            }
        }
        // update content type search map
        Set<Content> setTypes = new HashSet<Content>();
        if(this.searchTypeMap.get(type) == null || !this.searchTypeMap.containsKey(type)){                       
            setTypes.add(c);
            this.searchTypeMap.put(type, setTypes);
        }
        else{
            this.searchTypeMap.get(type).add(c);
        }
    }
   
    /**
     * Creates new Content and adds to content map and search maps if it doesn't already exist.
     * 
     * @param accessToken token restricting access to add data
     * @param contentData Content data
     * @param type type of Content
     * @throws CatalogException when an error is encountered creating the Content
     */
    private void addContent(String accessToken, String[] contentData, String type) 
    	throws CatalogException, PermissionException, InvalidAccessTokenException{
		try{
	    	AuthenticationServiceAPI as = AuthenticationServiceAPI.getInstance();
			String perm = "create_product";
			boolean hasAccess = as.verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", null, perm);
			}			
		} catch(InvalidAccessTokenException e){
			throw e;
		}          
    	String id;
        String name;
        String desc;
        String author;
        int rating;
        Set<String> cats = new HashSet<String>();
        Set<Country> countries = new HashSet<Country>();
        Set<Device> devices = new HashSet<Device>();
        float price;
        Set<String> langs = new HashSet<String>();
        String url;
        byte size;
        String delims = "\\|";
       
        if(type.equals("application") || type.equals("ringtone") || type.equals("wallpaper")){
            if((type.equals("application") && contentData.length == 13) ||
               !type.equals("application") && contentData.length == 12){
                id = (contentData[1].toLowerCase()).trim();
                name = contentData[2].trim();
                desc = contentData[3].trim();
                author = contentData[4].trim();
                try{
                    rating = Integer.parseInt(contentData[5].trim());                   
                }
                catch(NumberFormatException nfe){
                    throw new CatalogException("Error adding to Catalog: Invalid rating format", 
                    						   "Content", contentData[5].trim());                   
                }

                String[] parseCats = (contentData[6].trim()).split(delims);
                for(int i = 0; i < parseCats.length; i++){
                    cats.add((parseCats[i].toLowerCase()).trim());
                }
                
                String[] parseCountries = (contentData[7].trim()).split(delims);
                for(int i = 0; i < parseCountries.length; i++){
                    String c = (parseCountries[i].toUpperCase()).trim();
                    if(this.countryMap.get(c) == null){
                        throw new CatalogException("Error adding to Catalog: Invalid country in list",
                                                   "Content", c);
                    }
                    else{
                        countries.add(this.countryMap.get(c));
                    }
                }
                
                String[] parseDevices = (contentData[8].trim()).split(delims);
                for(int i = 0; i < parseDevices.length; i++){
                    String d = (parseDevices[i].toLowerCase()).trim();
                    if(this.deviceMap.get(d) == null){
                        throw new CatalogException("Error adding to Catalog: Invalid device in list", 
                        						   "Content", d);
                    }
                    else{
                        devices.add(this.deviceMap.get(d));
                    }
                }
                
                try{
                    price = Float.parseFloat(contentData[9].trim());
                }
                catch(NumberFormatException nfe){
                    throw new CatalogException("Error adding to Catalog: Invalid price format", 
                    						   "Content", contentData[9].trim());
                }       
                
                String[] parseLangs = (contentData[10].trim()).split(delims);
                for(int i = 0; i < parseLangs.length; i++){
                    langs.add((parseLangs[i].toLowerCase()).trim());
                }
                
                url = contentData[11].trim();
               
                if(this.contentMap.get(id) == null){
                    if(type.equals("application")){
                        try{
                            size = (byte) Integer.parseInt(contentData[12].trim());                       
                        }
                        catch(NumberFormatException nfe){
                            throw new CatalogException("Error adding to Catalog: Invalid size format", 
                            						   "Content", contentData[12].trim());                       
                        }
                        Application a = new Application(id, name, desc, cats, author, rating, price, langs,
                                                        url, countries, devices, size);
                        this.contentMap.put(id, a);                       
                        updateSearchMaps(a, "application");
                       
                        System.out.println("Application " + id + " was added!");
                    }
                    else if(type.equals("ringtone")){
                        RingTone r = new RingTone(id, name, desc, cats, author, rating, price, langs,
                                                  url, countries, devices);
                        this.contentMap.put(id, r);          
                        updateSearchMaps(r, "ringtone");
                       
                        System.out.println("RingTone " + id + " was added!");
                    }
                    else{
                        Wallpaper w = new Wallpaper(id, name, desc, cats, author, rating, price, langs,
                                  url, countries, devices);
                        this.contentMap.put(id, w);               
                        updateSearchMaps(w, "wallpaper");
                       
                        System.out.println("Wallpaper " + id + " was added!");
                    }
                }
                else{
                    System.out.println("Content" + id + " is already in system.");
                }
            }
            else{
                //
                throw new CatalogException("Error adding to Catalog: Missing Content fields", 
                						   "Content", null);           
            }           
        }
        else{
            throw new CatalogException("Error adding to Catalog: Invalid content type", "Content", type);
        }
    }
    
    /**
     * Retrieves the Content with the given identifier from the contentMap
     * 
     * @param id id of the Content
     * @return Content object having the id
     */
    public Content getProduct(String id){
    	return this.contentMap.get(id);
    }
    
    /**
     * 
     * @param criteria search criteria
     * @return Content matching the criteria
     * @throws SearchEngineException when error is encountered processing the search
     */
    public Set<Content> executeSearch(String[] criteria) throws SearchEngineException{
        int countCriteria = 0;
        String delims = "\\|";
        List<Set<Content>> resultSets = new ArrayList<Set<Content>>();
        Set<Content> finalResults = new HashSet<Content>();
        
        // run category search
        if(!(criteria[0].trim()).equals("")){
            countCriteria++;
            String[] parseCats = (criteria[0].trim()).split(delims);
            Set<Content> setCat = new HashSet<Content>();
            for(int i = 0; i < parseCats.length; i++){
                String c = (parseCats[i].toLowerCase()).trim();
                if(this.searchCatMap.containsKey(c)){
                    setCat.addAll(this.searchCatMap.get(c));
                }
            }
            resultSets.add(setCat);
        }
        // run text search
        if(!(criteria[1].trim()).equals("")){  
            countCriteria++;
            String[] parseText = (criteria[1].trim()).split(" +");
            Set<Content> setText = new HashSet<Content>();
            for(int i = 0; i < parseText.length; i++){
                String t = (parseText[i].toLowerCase()).trim();
                if(this.searchTextMap.containsKey(t)){
                    setText.addAll(this.searchTextMap.get(t));
                }
            }
            resultSets.add(setText);
        }
        // run minimum rating search
        if(!(criteria[2].trim()).equals("")){
            countCriteria++;
            Set<Content> setRating = new HashSet<Content>();
            try{
                int rating = Integer.parseInt(criteria[2].trim());
                for(int i=rating; i<=5; i++){
                	if(this.searchRatingMap.containsKey(i) && this.searchRatingMap.get(i) != null){
                    setRating.addAll(this.searchRatingMap.get(i));
                	}
            	}
                resultSets.add(setRating);
            }
            catch(NumberFormatException nfe){
                throw new SearchEngineException("Error executing search: Invalid rating format", 
                								null, null);
            }
        }
        // run maximum price search
        if(!(criteria[3].trim()).equals("")){
            countCriteria++;
            Set<Content> setPrice = new HashSet<Content>();
            try{
                float price = Float.parseFloat(criteria[3].trim());
                int maxprice = Math.round(price);
                for(int i=maxprice; i>=0; i--){
	                if(this.searchPriceMap.containsKey(i) && this.searchPriceMap.get(i) != null){
	                    setPrice.addAll(this.searchPriceMap.get(i));
	                }
                }
                resultSets.add(setPrice);
            }
            catch(NumberFormatException nfe){
                throw new SearchEngineException("Error executing search: Invalid price format", null, null);
            }
        }
        // run language search
        if(!(criteria[4].trim()).equals("")){
            countCriteria++;
            String[] parseLangs = (criteria[4].trim()).split(delims);
            Set<Content> setLang = new HashSet<Content>();
            for(int i = 0; i < parseLangs.length; i++){
                String l = (parseLangs[i].toLowerCase()).trim();
                if(this.searchLangMap.containsKey(l)){
                    setLang.addAll(this.searchLangMap.get(l));
                }
            }
            resultSets.add(setLang);
        }       
        // run country search
        if(!(criteria[5].trim()).equals("")){
            countCriteria++;
            String ctry = (criteria[5].toUpperCase()).trim();
            Set<Content> setCtry = new HashSet<Content>();
            if(this.searchCountryMap.containsKey(ctry)){
                setCtry = this.searchCountryMap.get(ctry);
                resultSets.add(setCtry);
            }      
        }         
        // run device search
        if(!(criteria[6].trim()).equals("")){
            countCriteria++;
            String dev = (criteria[6].toLowerCase()).trim();
            Set<Content> setDev = new HashSet<Content>();
            if(this.searchDeviceMap.containsKey(dev)){
                setDev = this.searchDeviceMap.get(dev);
                resultSets.add(setDev);
            }      
        }       
        // run content type search
        if(!(criteria[7].trim()).equals("")){  
            countCriteria++;
            String[] parseCont = (criteria[7].trim()).split(delims);
            Set<Content> setCont = new HashSet<Content>();
            for(int i = 0; i < parseCont.length; i++){
                String ct = (parseCont[i].toLowerCase()).trim();
                if(this.searchTypeMap.containsKey(ct)){
                    setCont.addAll(this.searchTypeMap.get(ct));
                }
            }
            resultSets.add(setCont);
        }
        
        if(countCriteria == 0){
            throw new SearchEngineException("Error executing search: You must provide at least one search " +
            								"criterion.", null, null);
        }
        else if(resultSets.isEmpty() || resultSets.contains(null)){
            return null;
        }    
        else{
            // get the intersection of the results and display them
            // help from http://stackoverflow.com/questions/4009205/how-to-intersect-multiple-sets
            finalResults.addAll(resultSets.get(0));
            for(int i = 1; i < resultSets.size(); i++){
            	finalResults.retainAll(resultSets.get(i));
            }
            if(finalResults.isEmpty()){
                return null;
            }
            else{
                return finalResults;
            }
        }
    }
   
    /**
     * Prints search results to stdout.
     * 
     * @param results search results
     */
    private void displayResults(Set<Content> results){
        // iterate over the Content objects and print their properties
        Iterator<Content> itRes = results.iterator();
        System.out.println("\nResults:\n");
        while(itRes.hasNext()){
            System.out.println((itRes.next()).toString());
        }
    }
}
