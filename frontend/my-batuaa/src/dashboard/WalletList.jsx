import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import CardActionArea from '@mui/material/CardActionArea';
import CardActions from '@mui/material/CardActions';
import { Box, IconButton } from '@mui/material';
import copyLogo from '../images/copy_logo.png';
import { Add, Wallet } from '@mui/icons-material';
import AddMoney from './AddMoney.jsx';
import  walletLogo from '../images/wallet.png'
import transferLogo from '../images/transfer_logo.png'
import shoppinyLogo from '../images/shopying.png'
import "./WalletList.css";
import AddWallet from './AddWallet.jsx';

export default function WalletList() {
return (
    <Box className="wallet-list-container">
    <Card sx={{ maxWidth: 345 ,color:"#0F3A6E", borderRadius: 4, boxShadow: 3, borderColor: '#9828a2ff' , borderBlockColor: '#ba357cff' }}>
      <CardActionArea sx={{ backgroundColor: '#0F3A6E'}}>
 
        <CardContent>
          <Typography gutterBottom variant="h5" align='right' component="div"sx={{ color: 'white' }}>
            Wallet
          </Typography>
          <Typography variant="body2" align='left' sx={{ color: 'white' }}>
            Name: Bhooli nagar
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1,  flexDirection: 'row'  }}>
          <Typography variant="body2" align='left' sx={{ color: 'white ' }}>
            Wallet Id: WAL238932DS
    
          </Typography>
          <IconButton aria-label="copy" sx={{color:'#439b8bff' ,backgroundColor:'white', align:"right"   }} >
            <img src={copyLogo} alt="Copy" style={{ width: 20, height: 20 }} />
          </IconButton>
          </Box>
          <Typography variant="body2" align='left' sx={{ color: 'white ' }}>
            Balance: 1000.00 INR
          </Typography>
        </CardContent>
      </CardActionArea >
      <CardActions  className="wallet-card-actions">
        <Button size="medium" color="primary" className="add-wallet-btn"
          onClick={() => {
            window.location.href = '/addmoney';
            // Navigate to AddMoney component
            
          }}
        >Add Money</Button>
      </CardActions>
    </Card>

   <Card className="wallet-card" sx={{borderRadius:4 ,width:270}}>
      <CardActionArea>
        <img src={walletLogo} alt="Wallet" className="wallet-image" />
      </CardActionArea>

      <CardActions className="wallet-card-actions">
        <Button size="medium" color="primary" className="add-wallet-btn"
          onClick={() => {
            window.location.href = '/add-wallet';
            // Navigate to AddWallet component
          }}  
        >
          Add new Wallet
        </Button>
      </CardActions>
    </Card>  

     <Card className="wallet-card" sx={{borderRadius:4 ,width:270}}>
      <CardActionArea>
        <img src={transferLogo} alt="Wallet" className="wallet-image" />
      </CardActionArea>

      <CardActions className="wallet-card-actions">
        <Button size="medium" color="primary" className="add-wallet-btn">
          Transfer Money
        </Button>
      </CardActions>
    </Card>  
    
     <Card className="wallet-card" sx={{borderRadius:4 ,width:270}}>
      <CardActionArea>
        <img src={shoppinyLogo} alt="Wallet" className="wallet-image" />
      </CardActionArea>

      <CardActions className="wallet-card-actions">
        <Button size="medium" color="primary" className="add-wallet-btn" 
        
        >
            Shopping
        </Button>
      </CardActions>
    </Card>  
    
  </Box>
  );

}
