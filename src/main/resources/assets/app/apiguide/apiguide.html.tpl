<div navbar></div>
<div class="container" >

<h1>API</h1>
<p>The Schema.TF API is designed to provide developers with more useful targeted versions of the steam schema. You can call the API directly via REST, or if you're developing in Java, you can use our client library (see below)</p>
<div class="panel panel-active">
  <div class="panel-heading">
    <h3 class="panel-title">Schema.TF Client Library (Maven)</h3>
  </div>
  <div class="panel-body">
   
    
    <!-- Place this tag where you want the button to render. -->
<a class="github-button" href="https://github.com/danielburrell/schema.tf/" data-style="mega" aria-label="View danielburrell/schema.tf on GitHub">View on GitHub</a>
<!-- Place this tag right after the last button or just before your close body tag. -->
<script async defer id="github-bjs" src="https://buttons.github.io/buttons.js"></script>
 <p>Gradle/Maven dependency here: <code>{uk.co.solong:schematf-client:0.7}</code></p>
  </div>
</div>
<table class="table">
    <thead>
        <tr>
            <th>URL</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="active">/api/getRawSchema</td>
            <td class="active">Retrieves the latest current schema as provided by steam. Schemas are refreshed every 60 seconds.</td>
        </tr>
        <tr>
            <td class="active">/api/getAllQualitiesSimple</td>
            <td class="active">Retrieves the latest qualities enriched with additional data such as the colour.</td>
        </tr>
        <tr>
            <td class="active">/api/getAllQualitiesRaw</td>
            <td class="active">Retrieves the latest qualities schema as provided by steam.</td>
        </tr>
        <tr>
            <td class="active">/api/getAllItemsRaw</td>
            <td class="active">Retrieves the list of all items in the game as provided by steam.</td>
        </tr>
        <tr>
            <td class="active">/api/getAllItemsCdn</td>
            <td class="active">Retrieves the list of all items in the game with the image location substituted to an appropriate CDN.</td>
        </tr>
    </tbody>
</table>
  
</div>