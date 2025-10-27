
import { useEffect, useState } from "react";
import axios from "axios";
import BasicCardFourCorners from "./BasicCardFourCorners"
import { Box, Stack } from "@mui/material";
import Typography from '@mui/material/Typography';


export default function TransactionCard()
{

 /*let products=[
                {   
                    "id":10,
                    "itemname":"Sweets",
                    "url":"image url of sweet"
                },
                   {   
                    "id":20,
                    "itemname":"Mobile",
                    "url":"image url of mobile"
                },
                   {   
                    "id":30,
                    "itemname":"Jeans",
                    "url":"image url of jeans"
                }

 ]*/

let [transactions,setTransactions]=useState([])
 
useEffect(
  ()=>{
   //axios.get("http://localhost:8084/api/products")
    axios.get("http://localhost:8082/transaction/api/v2/all-transactions?emailId=sakshi@gmail.com&walletId=1")
      .then(
        (res)=>
          {
            setTransactions(res.data)
            console.log(res.data)
          }
      )
      .catch(
        (err)=>console.log(err)
      )

  },[]

)



return(
           

           

           <div>
          
           <Typography variant="h5" sx={{ mb: 2 }} align="left">
               Recent Transactions
           </Typography>
           <Box sx={{ backgroundColor: '#f5f5f5', borderRadius: 2, p: 4, width: '70%' }}>
            
           <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Typography variant="subtitle1" align="left">Recent Transactions</Typography>
              <Typography variant="subtitle1" align="left">October 2025</Typography>
            </Box>
            <Typography variant="subtitle1" align="right" sx={{ alignSelf: 'flex-start' }}>
               View all
            </Typography>
           </Box>


           <Stack spacing={3} sx={{mt:4}}>
             {transactions.map((transaction) => (
            <BasicCardFourCorners transaction={transaction}/>
             
           ))}
           
           </Stack>
           </Box>
           </div>
             



)


}
